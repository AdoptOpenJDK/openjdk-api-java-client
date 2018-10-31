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

package net.adoptopenjdk.v2;

import net.adoptopenjdk.spi.AOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A provider of HTTP connections.
 */

public final class AOv2HTTPConnections implements AOv2HTTPConnectionsType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOv2HTTPConnections.class);

  /**
   * Construct a new provider.
   */

  private AOv2HTTPConnections()
  {

  }

  /**
   * Construct a new provider.
   *
   * @return A new connection provider
   */

  public static AOv2HTTPConnectionsType create()
  {
    return new AOv2HTTPConnections();
  }

  @Override
  public AOv2HTTPConnectionType head(
    final URI uri,
    final Map<String, String> headers)
    throws AOException
  {
    Objects.requireNonNull(uri, "uri");
    Objects.requireNonNull(headers, "headers");

    final HttpClient client =
      HttpClient.newBuilder()
        .build();

    final HttpRequest.Builder request_builder =
      HttpRequest.newBuilder(uri)
        .method("HEAD", HttpRequest.BodyPublishers.noBody());

    headers.forEach(request_builder::setHeader);

    final HttpRequest request = request_builder.build();

    try {
      final HttpResponse<InputStream> response =
        client.send(request, HttpResponse.BodyHandlers.ofInputStream());

      checkResponseCode(uri, response);
      return new Connection(response);
    } catch (final IOException | InterruptedException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  @Override
  public AOv2HTTPConnectionType get(
    final URI uri,
    final Map<String, String> headers)
    throws AOException
  {
    Objects.requireNonNull(uri, "uri");
    Objects.requireNonNull(headers, "headers");

    final HttpClient client =
      HttpClient.newBuilder()
        .build();

    final HttpRequest.Builder request_builder =
      HttpRequest.newBuilder(uri)
        .method("GET", HttpRequest.BodyPublishers.noBody());

    headers.forEach(request_builder::setHeader);

    final HttpRequest request = request_builder.build();

    try {
      final HttpResponse<InputStream> response =
        client.send(request, HttpResponse.BodyHandlers.ofInputStream());

      checkResponseCode(uri, response);
      return new Connection(response);
    } catch (final IOException | InterruptedException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  private static void checkResponseCode(
    final URI uri,
    final HttpResponse<InputStream> response)
    throws AOv2HTTPException
  {
    final int code = response.statusCode();
    if (code >= 400) {
      throw new AOv2HTTPException(
        code,
        Integer.toString(code),
        uri,
        reportFor(response),
        response.headers().map());
    }
  }

  private static AOv2HTTPProblemReport reportFor(
    final HttpResponse<InputStream> response)
  {
    final String type =
      response.headers()
        .firstValue("content-type")
        .orElse("application/octet-stream");

    try {
      try (InputStream stream = response.body()) {
        if (stream != null) {
          return AOv2HTTPProblemReport.of(type, stream.readAllBytes());
        }
        return fallbackReport(type);
      }
    } catch (final IOException e) {
      LOG.error("error reading problem report: ", e);
      return fallbackReport(type);
    }
  }

  private static AOv2HTTPProblemReport fallbackReport(final String type)
  {
    return AOv2HTTPProblemReport.of(type, new byte[0]);
  }

  private static final class Connection implements AOv2HTTPConnectionType
  {
    private final HttpResponse<InputStream> response;

    Connection(final HttpResponse<InputStream> in_response)
    {
      this.response = Objects.requireNonNull(in_response, "response");
    }

    @Override
    public InputStream input()
    {
      return this.response.body();
    }

    @Override
    public Map<String, List<String>> headers()
    {
      return this.response.headers().map();
    }

    @Override
    public void close()
    {

    }
  }
}
