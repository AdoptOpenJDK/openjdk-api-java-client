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

package net.adoptopenjdk.v1;

import net.adoptopenjdk.spi.AOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A provider of HTTP connections.
 */

public final class AOv1HTTPConnections implements AOv1HTTPConnectionsType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOv1HTTPConnections.class);

  private final Supplier<HostnameVerifier> verifiers;

  /**
   * Construct a new provider.
   *
   * @param in_verifiers A supplier of hostname verifiers
   */

  private AOv1HTTPConnections(
    final Supplier<HostnameVerifier> in_verifiers)
  {
    this.verifiers =
      Objects.requireNonNull(in_verifiers, "verifiers");
  }

  /**
   * Construct a new provider.
   *
   * @return A new connection provider
   */

  public static AOv1HTTPConnectionsType create()
  {
    return new AOv1HTTPConnections(
      HttpsURLConnection::getDefaultHostnameVerifier);
  }

  /**
   * Construct a new provider.
   *
   * @param verifiers A supplier of hostname verifiers
   *
   * @return A new connection provider
   */

  public static AOv1HTTPConnectionsType createWithVerifiers(
    final Supplier<HostnameVerifier> verifiers)
  {
    return new AOv1HTTPConnections(
      Objects.requireNonNull(verifiers, "verifiers"));
  }

  private static void checkResponseCode(
    final URI uri,
    final HttpURLConnection connection)
    throws IOException, AOv1HTTPException
  {
    final int code = connection.getResponseCode();
    if (code >= 400) {
      throw new AOv1HTTPException(
        code,
        connection.getResponseMessage(),
        uri,
        reportFor(connection));
    }
  }

  private static AOv1HTTPProblemReport reportFor(
    final HttpURLConnection connection)
  {
    final String type = connection.getContentType();

    try {
      try (InputStream stream = connection.getErrorStream()) {
        if (stream != null) {
          return AOv1HTTPProblemReport.of(
            type != null ? type : "application/octet-stream",
            stream.readAllBytes());
        }
        return fallbackReport(type);
      }
    } catch (final IOException e) {
      LOG.error("error reading problem report: ", e);
      return fallbackReport(type);
    }
  }

  private static AOv1HTTPProblemReport fallbackReport(final String type)
  {
    return AOv1HTTPProblemReport.of(type, new byte[0]);
  }

  private static void setHeaders(
    final Map<String, String> headers,
    final HttpURLConnection connection)
  {
    for (final String name : headers.keySet()) {
      connection.setRequestProperty(name, headers.get(name));
    }

    connection.setRequestProperty("accept-version", "1.0.0");
    connection.setRequestProperty("Accept", "*/*");
  }

  private static void checkURI(final URI uri)
    throws AOException
  {
    if (!uri.getScheme().startsWith("http")) {
      throw new AOException("URI is not an http or https URI");
    }
  }

  @Override
  public AOv1HTTPConnectionType head(
    final URI uri,
    final Map<String, String> headers)
    throws AOException
  {
    Objects.requireNonNull(uri, "uri");
    Objects.requireNonNull(headers, "headers");

    try {
      checkURI(uri);

      LOG.debug("HEAD {}", uri);

      final URL url = uri.toURL();
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      setHeaders(headers, connection);
      setHostnameVerifierIfNecessary(this.verifiers.get(), connection);

      connection.setInstanceFollowRedirects(true);
      connection.setRequestMethod("HEAD");
      connection.connect();

      checkResponseCode(uri, connection);
      return new Connection(connection);
    } catch (final IOException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  private static void setHostnameVerifierIfNecessary(
    final HostnameVerifier verifier,
    final HttpURLConnection connection)
  {
    if (connection instanceof HttpsURLConnection) {
      final HttpsURLConnection connection_s = (HttpsURLConnection) connection;
      connection_s.setHostnameVerifier(verifier);
    }
  }

  @Override
  public AOv1HTTPConnectionType get(
    final URI uri,
    final Map<String, String> headers)
    throws AOException
  {
    Objects.requireNonNull(uri, "uri");
    Objects.requireNonNull(headers, "headers");

    try {
      checkURI(uri);

      LOG.debug("GET {}", uri);

      final URL url = uri.toURL();
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      setHeaders(headers, connection);
      setHostnameVerifierIfNecessary(this.verifiers.get(), connection);

      connection.setInstanceFollowRedirects(true);
      connection.setRequestMethod("GET");
      connection.connect();

      checkResponseCode(uri, connection);
      return new Connection(connection);
    } catch (final IOException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  private static final class Connection implements AOv1HTTPConnectionType
  {
    private final HttpURLConnection connection;

    Connection(final HttpURLConnection in_connection)
    {
      this.connection = Objects.requireNonNull(in_connection, "connection");
    }

    @Override
    public InputStream input()
      throws IOException
    {
      return this.connection.getInputStream();
    }

    @Override
    public Map<String, List<String>> headers()
    {
      return this.connection.getHeaderFields();
    }

    @Override
    public void close()
    {

    }
  }
}
