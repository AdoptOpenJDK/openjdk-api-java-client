/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.adoptopenjdk.v3.vanilla;

import net.adoptopenjdk.v3.api.AOV3Architecture;
import net.adoptopenjdk.v3.api.AOV3ClientType;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3RequestAssetsForReleaseType;
import net.adoptopenjdk.v3.api.AOV3RequestReleaseNamesType;
import net.adoptopenjdk.v3.api.AOV3RequestReleaseVersionsType;
import net.adoptopenjdk.v3.api.AOV3RequestReleasesType;
import net.adoptopenjdk.v3.api.AOV3SortOrder;
import net.adoptopenjdk.v3.api.AOV3Vendor;
import net.adoptopenjdk.v3.api.AOV3VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

final class AOV3Client implements AOV3ClientType, AOV3ClientInternalType
{
  private static final Logger LOG = LoggerFactory.getLogger(AOV3Client.class);
  private final String baseURI;
  private final HttpClient client;
  private final AOV3ResponseParsersType parsers;
  private final AOV3MessagesType messages;

  AOV3Client(
    final String inBaseURI,
    final HttpClient inClient,
    final AOV3MessagesType inMessages,
    final AOV3ResponseParsersType inParsers)
  {
    this.baseURI =
      Objects.requireNonNull(inBaseURI, "baseURI");
    this.client =
      Objects.requireNonNull(inClient, "client");
    this.messages =
      Objects.requireNonNull(inMessages, "messages");
    this.parsers =
      Objects.requireNonNull(inParsers, "parsers");
  }

  private static void logReceivedHeaders(
    final HttpResponse<InputStream> response)
  {
    if (LOG.isDebugEnabled()) {
      final var headers = response.headers().map();
      for (final var entry : headers.entrySet()) {
        for (final var value : entry.getValue()) {
          LOG.debug("← {}: {}", entry.getKey(), value);
        }
      }
    }
  }

  private static void logRequestHeaders(
    final HttpHeaders request)
  {
    if (LOG.isDebugEnabled()) {
      final var headers = request.map();
      for (final var entry : headers.entrySet()) {
        for (final var value : entry.getValue()) {
          LOG.debug("→ {}: {}", entry.getKey(), value);
        }
      }
    }
  }

  private static String userAgentHeader()
  {
    final var clazzPackage = AOV3Client.class.getPackage();
    final var packageInfo =
      Optional.ofNullable(clazzPackage.getImplementationVersion())
        .orElse("0.0.0");

    return String.format("net.adoptopenjdk.v3.vanilla %s", packageInfo);
  }

  private static InputStream streamOf(
    final HttpResponse<InputStream> response)
    throws IOException
  {
    final var encoding =
      response.headers()
        .firstValue("Content-Encoding")
        .orElse("");

    if (encoding.contains("gzip")) {
      return new GZIPInputStream(response.body());
    }
    return response.body();
  }

  @Override
  public void close()
  {

  }

  @Override
  public AOV3RequestReleasesType availableReleases(
    final Consumer<AOV3Error> errorReceiver)
  {
    return new AOV3RequestReleases(errorReceiver, this);
  }

  @Override
  public AOV3RequestReleaseNamesType releaseNames(
    final Consumer<AOV3Error> errorReceiver,
    final BigInteger page,
    final BigInteger pageSize,
    final Optional<AOV3ReleaseKind> releaseKind,
    final Optional<AOV3SortOrder> sortOrder,
    final Optional<AOV3Vendor> vendor,
    final Optional<AOV3VersionRange> versionRange)
  {
    return new AOV3RequestReleaseNames(
      this,
      errorReceiver,
      page,
      pageSize,
      releaseKind,
      sortOrder,
      vendor,
      versionRange
    );
  }

  @Override
  public AOV3RequestReleaseVersionsType releaseVersions(
    final Consumer<AOV3Error> errorReceiver,
    final BigInteger page,
    final BigInteger pageSize,
    final Optional<AOV3ReleaseKind> releaseKind,
    final Optional<AOV3SortOrder> sortOrder,
    final Optional<AOV3Vendor> vendor,
    final Optional<AOV3VersionRange> versionRange)
  {
    return new AOV3RequestReleaseVersions(
      this,
      errorReceiver,
      page,
      pageSize,
      releaseKind,
      sortOrder,
      vendor,
      versionRange
    );
  }

  @Override
  public AOV3RequestAssetsForReleaseType assetsForRelease(
    final Consumer<AOV3Error> errorReceiver,
    final BigInteger page,
    final BigInteger pageSize,
    final BigInteger version,
    final AOV3ReleaseKind releaseKind,
    final Optional<AOV3Architecture> architecture,
    final Optional<AOV3HeapSize> heapSize,
    final Optional<AOV3ImageKind> imageKind,
    final Optional<AOV3JVMImplementation> jvmImplementation,
    final Optional<AOV3OperatingSystem> operatingSystem,
    final Optional<String> project,
    final Optional<AOV3SortOrder> sortOrder,
    final Optional<AOV3Vendor> vendor)
  {
    return new AOV3RequestAssetsForRelease(
      this,
      errorReceiver,
      page,
      pageSize,
      version,
      releaseKind,
      architecture,
      heapSize,
      imageKind,
      jvmImplementation,
      operatingSystem,
      project,
      sortOrder,
      vendor
    );
  }

  @Override
  public AOV3ResponseParserType parserForURI(
    final Consumer<AOV3Error> errorReceiver,
    final URI sourceURI)
    throws IOException, InterruptedException
  {
    Objects.requireNonNull(errorReceiver, "errorReceiver");
    Objects.requireNonNull(sourceURI, "sourceURI");

    final var response = this.send(sourceURI);
    return this.parsers.createParser(
      errorReceiver,
      response.uri(),
      streamOf(response)
    );
  }

  private HttpResponse<InputStream> send(
    final URI sourceURI)
    throws IOException, InterruptedException
  {
    LOG.info("GET {}", sourceURI);

    final var request =
      HttpRequest.newBuilder(sourceURI)
        .header("Accept-Encoding", "gzip")
        .header("User-Agent", userAgentHeader())
        .GET()
        .build();

    logRequestHeaders(request.headers());

    final var response =
      this.client.send(request, HttpResponse.BodyHandlers.ofInputStream());

    logReceivedHeaders(response);
    if (response.statusCode() >= 400) {
      throw new IOException(
        this.messages.requestFailed(response.statusCode(), response.uri()));
    }

    return response;
  }

  @Override
  public String baseURI()
  {
    return this.baseURI;
  }
}
