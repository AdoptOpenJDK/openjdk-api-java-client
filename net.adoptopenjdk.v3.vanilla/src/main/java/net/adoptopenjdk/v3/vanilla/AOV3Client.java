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
import net.adoptopenjdk.v3.api.AOV3Exception;
import net.adoptopenjdk.v3.api.AOV3ExceptionHTTPRequestFailed;
import net.adoptopenjdk.v3.api.AOV3ExceptionHTTPRequestIOFailed;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3RequestAssetsForLatestType;
import net.adoptopenjdk.v3.api.AOV3RequestAssetsForReleaseType;
import net.adoptopenjdk.v3.api.AOV3RequestBinaryForLatestType;
import net.adoptopenjdk.v3.api.AOV3RequestBinaryForReleaseType;
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

    checkClientNoRedirects(this.messages, inClient);
  }

  private static void checkClientNoRedirects(
    final AOV3MessagesType messages,
    final HttpClient inClient)
  {
    switch (inClient.followRedirects()) {
      case NEVER: {
        break;
      }
      case ALWAYS:
      case NORMAL: {
        throw new IllegalStateException(
          messages.httpClientNoRedirects(inClient.followRedirects()));
      }
    }
  }

  private static void logReceivedHeaders(
    final HttpResponse<?> response)
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
  public AOV3RequestAssetsForLatestType assetsForLatest(
    final Consumer<AOV3Error> errorReceiver,
    final BigInteger version,
    final AOV3JVMImplementation jvmImplementation)
  {
    return new AOV3RequestAssetsForLatest(
      this,
      errorReceiver,
      version,
      jvmImplementation
    );
  }

  @Override
  public AOV3RequestBinaryForLatestType binaryForLatest(
    final Consumer<AOV3Error> errorReceiver,
    final AOV3Architecture architecture,
    final BigInteger version,
    final AOV3HeapSize heapSize,
    final AOV3ImageKind imageKind,
    final AOV3JVMImplementation jvmImplementation,
    final AOV3OperatingSystem operatingSystem,
    final AOV3ReleaseKind releaseKind,
    final AOV3Vendor vendor,
    final Optional<String> project)
  {
    return new AOV3RequestBinaryForLatest(
      this,
      architecture,
      version,
      heapSize,
      imageKind,
      jvmImplementation,
      operatingSystem,
      releaseKind,
      vendor,
      project
    );
  }

  @Override
  public AOV3RequestBinaryForReleaseType binaryForRelease(
    final Consumer<AOV3Error> errorReceiver,
    final String releaseName,
    final AOV3OperatingSystem operatingSystem,
    final AOV3Architecture architecture,
    final AOV3ImageKind imageKind,
    final AOV3JVMImplementation jvmImplementation,
    final AOV3HeapSize heapSize,
    final AOV3Vendor vendor,
    final Optional<String> project)
  {
    return new AOV3RequestBinaryForRelease(
      this,
      architecture,
      heapSize,
      imageKind,
      jvmImplementation,
      operatingSystem,
      vendor,
      project,
      releaseName
    );
  }

  @Override
  public AOV3ResponseParserType parserForURI(
    final Consumer<AOV3Error> errorReceiver,
    final URI sourceURI)
    throws AOV3Exception, InterruptedException
  {
    Objects.requireNonNull(errorReceiver, "errorReceiver");
    Objects.requireNonNull(sourceURI, "sourceURI");

    try {
      final var response = this.send(sourceURI);
      return this.parsers.createParser(
        errorReceiver,
        response.uri(),
        streamOf(response)
      );
    } catch (final IOException e) {
      throw new AOV3ExceptionHTTPRequestIOFailed(sourceURI, e);
    }
  }

  public URI uriFor(
    final String uri)
    throws
    InterruptedException,
    AOV3ExceptionHTTPRequestIOFailed,
    AOV3ExceptionHTTPRequestFailed
  {
    LOG.info("GET {}", uri);

    final var sourceURI = URI.create(uri);
    final var request =
      HttpRequest.newBuilder(sourceURI)
        .header("Accept-Encoding", "gzip")
        .header("User-Agent", userAgentHeader())
        .GET()
        .build();

    logRequestHeaders(request.headers());

    final HttpResponse<Void> response;
    try {
      response =
        this.client.send(request, HttpResponse.BodyHandlers.discarding());
    } catch (final IOException e) {
      throw new AOV3ExceptionHTTPRequestIOFailed(sourceURI, e);
    }

    logReceivedHeaders(response);

    if (response.statusCode() == 307) {
      final var location = response.headers().firstValue("Location");
      if (location.isEmpty()) {
        throw new AOV3ExceptionHTTPRequestFailed(
          response.statusCode(),
          response.uri(),
          this.messages.locationMissing(response.statusCode(), response.uri()),
          response.headers().map()
        );
      }
      return URI.create(location.get());
    }

    throw new AOV3ExceptionHTTPRequestFailed(
      response.statusCode(),
      response.uri(),
      this.messages.requestFailed(response.statusCode(), response.uri()),
      response.headers().map()
    );
  }

  private HttpResponse<InputStream> send(
    final URI sourceURI)
    throws
    InterruptedException,
    AOV3ExceptionHTTPRequestFailed,
    AOV3ExceptionHTTPRequestIOFailed
  {
    LOG.info("GET {}", sourceURI);

    final var request =
      HttpRequest.newBuilder(sourceURI)
        .header("Accept-Encoding", "gzip")
        .header("User-Agent", userAgentHeader())
        .GET()
        .build();

    logRequestHeaders(request.headers());

    final HttpResponse<InputStream> response;
    try {
      response =
        this.client.send(request, HttpResponse.BodyHandlers.ofInputStream());
    } catch (final IOException e) {
      throw new AOV3ExceptionHTTPRequestIOFailed(sourceURI, e);
    }

    logReceivedHeaders(response);
    if (response.statusCode() >= 400) {
      throw new AOV3ExceptionHTTPRequestFailed(
        response.statusCode(),
        response.uri(),
        this.messages.requestFailed(response.statusCode(), response.uri()),
        response.headers().map()
      );
    }

    return response;
  }

  @Override
  public String baseURI()
  {
    return this.baseURI;
  }
}
