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

import net.adoptopenjdk.spi.AOAPIRequestsType;
import net.adoptopenjdk.spi.AOAPIVersionProviderType;
import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The default implementation of the API 2.0 request provider.
 */

public final class AOv2Requests implements AOv2RequestsType
{
  private static final Logger LOG = LoggerFactory.getLogger(AOv2Requests.class);

  private static final String DEFAULT_HOSTNAME = "api.adoptopenjdk.net";
  private static final String DEFAULT_SERVER_SCHEME = "https";

  private final AOv2HTTPConnectionsType connections;
  private RateLimit rate_limit;
  private final AOv2Parser parser;
  private final ResourceBundle messages;

  private AOv2Requests(
    final AOv2Parser in_parser,
    final AOv2HTTPConnectionsType in_connections,
    final RateLimit in_rate_limit)
  {
    this.parser =
      Objects.requireNonNull(in_parser, "parser");
    this.connections =
      Objects.requireNonNull(in_connections, "connections");
    this.rate_limit =
      Objects.requireNonNull(in_rate_limit, "rate_limit");
    this.messages =
      ResourceBundle.getBundle("net/adoptopenjdk/v2/messages");
  }

  private static RateLimit checkRateLimit(
    final AOv2HTTPConnectionsType connections)
    throws AOException
  {
    Objects.requireNonNull(connections, "connections");

    final Map<String, String> props = standardProperties();
    final URI target =
      URI.create(
        new StringBuilder(64)
          .append(serverScheme())
          .append("://")
          .append(serverAddress())
          .append("/v2/")
          .toString());

    try (AOv2HTTPConnectionType connection = connections.head(target, props)) {
      return rateLimitForConnection(connection);
    } catch (final AOv2HTTPException e) {
      if (e.statusCode() == 404) {
        return rateLimitForHeaders(e.headers());
      }
      throw e;
    } catch (final IOException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  private static Map<String, String> standardProperties()
  {
    return Map.of("User-Agent", userAgent());
  }

  private static String userAgent()
  {
    final String v = AOv2Requests.class.getPackage().getImplementationVersion();
    return "net.adoptopenjdk.api " + ((v == null) ? "0.0.0" : v);
  }

  private static RateLimit rateLimitForConnection(
    final AOv2HTTPConnectionType connection)
  {
    return rateLimitForHeaders(connection.headers());
  }

  private static RateLimit rateLimitForHeaders(
    final Map<String, List<String>> headers)
  {
    final int remaining =
      integerValuedKey(headers, "X-RateLimit-Remaining", Integer.MAX_VALUE);

    final int retry;
    if (remaining == 0) {
      retry = integerValuedKey(headers, "Retry-After", 3600);
    } else {
      retry = 3600;
    }

    return new RateLimit(remaining, retry);
  }

  private static int integerValuedKey(
    final Map<String, List<String>> headers,
    final String key,
    final int default_value)
  {
    try {
      if (!headers.containsKey(key)) {
        LOG.debug("no header {}", key);
        return default_value;
      }

      return Integer.valueOf(headers.get(key).get(0)).intValue();
    } catch (final NumberFormatException e) {
      LOG.warn("unable to parse {}: ", key, e);
      return default_value;
    }
  }

  private static String serverScheme()
  {
    return System.getProperty(
      "net.adoptopenjdk.server_scheme", DEFAULT_SERVER_SCHEME);
  }

  private static String serverAddress()
  {
    return System.getProperty(
      "net.adoptopenjdk.server_address", DEFAULT_HOSTNAME);
  }

  private static String serverHostName()
  {
    return System.getProperty(
      "net.adoptopenjdk.server_host_name", DEFAULT_HOSTNAME);
  }

  /**
   * Create a new 2.0 request provider using the given HTTP connection provider.
   *
   * @param connections The HTTP connection provider
   *
   * @return A new request provider
   *
   * @throws AOException On initialization errors
   */

  public static AOv2RequestsType open(
    final AOv2HTTPConnectionsType connections)
    throws AOException
  {
    Objects.requireNonNull(connections, "connections");
    return new AOv2Requests(
      new AOv2Parser(),
      connections,
      checkRateLimit(connections));
  }

  /**
   * Create a new 2.0 request provider.
   *
   * @return A new provider
   *
   * @throws AOException On initialization errors
   */

  public static AOv2RequestsType open()
    throws AOException
  {
    return open(AOv2HTTPConnections.create());
  }

  @Override
  public int rateLimitRemaining()
  {
    return this.rate_limit.remaining;
  }

  private void checkRateLimitRemaining()
    throws AOException
  {
    if (this.rate_limit.remaining <= 0) {
      throw new AOException(
        MessageFormat.format(
          this.messages.getString("api_rate_limit_exceeded"),
          Integer.valueOf(this.rate_limit.retry_in)));
    }
  }

  @Override
  public List<AOVariant> variants()
    throws AOException
  {
    this.checkRateLimitRemaining();

    final URI target = URI.create(
      new StringBuilder(64)
        .append(serverScheme())
        .append("://")
        .append(serverAddress())
        .append("/v2/variants")
        .toString());

    try (AOv2HTTPConnectionType connection =
           this.connections.get(target, standardProperties())) {

      this.rate_limit = rateLimitForConnection(connection);
      try (InputStream stream = connection.input()) {
        return this.parser.parseVariants(target, stream);
      }
    } catch (final IOException e) {
      throw new AOException(e.getMessage(), e);
    }
  }

  @Override
  public List<AORelease> releasesForVariant(final String variant)
    throws AOException
  {
    Objects.requireNonNull(variant, "variant");
    return List.of();
  }

  @Override
  public List<AORelease> nightlyBuildsForVariant(final String variant)
    throws AOException
  {
    Objects.requireNonNull(variant, "variant");
    return List.of();
  }

  private static final class RateLimit
  {
    private final int remaining;
    private final int retry_in;

    RateLimit(
      final int in_remaining,
      final int in_retry_in)
    {
      this.remaining = in_remaining;
      this.retry_in = in_retry_in;
    }
  }

  /**
   * A provider for the {@link AOAPIVersionProviderType} interface.
   */

  public static final class Provider implements AOAPIVersionProviderType
  {
    /**
     * Construct a provider. This method is intended to be called by {@link
     * java.util.ServiceLoader}.
     */

    public Provider()
    {

    }

    /**
     * Create a new 2.0 request provider. This method is intended to be called by {@link
     * java.util.ServiceLoader}.
     *
     * @return A new provider
     */

    public static AOAPIVersionProviderType provider()
    {
      return new Provider();
    }

    @Override
    public int supportedMajorAPI()
    {
      return 2;
    }

    @Override
    public AOAPIRequestsType create()
      throws AOException
    {
      return open();
    }
  }
}
