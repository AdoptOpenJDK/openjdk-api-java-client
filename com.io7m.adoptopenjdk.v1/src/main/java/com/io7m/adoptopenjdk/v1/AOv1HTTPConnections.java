/*
 * Copyright © 2018 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.adoptopenjdk.v1;

import com.io7m.adoptopenjdk.spi.AOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A provider of HTTP connections.
 */

public final class AOv1HTTPConnections implements AOv1HTTPConnectionsType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOv1HTTPConnections.class);

  /**
   * Construct a new provider.
   */

  public AOv1HTTPConnections()
  {

  }

  private static void checkResponseCode(
    final URI uri,
    final HttpURLConnection connection)
    throws IOException, AOv1HTTPException
  {
    final int code = connection.getResponseCode();
    if (code >= 400) {
      throw new AOv1HTTPException(code, connection.getResponseMessage(), uri);
    }
  }

  private static void setHeaders(
    final Map<String, String> headers,
    final HttpURLConnection connection)
  {
    for (final String name : headers.keySet()) {
      connection.setRequestProperty(name, headers.get(name));
    }
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

      connection.setInstanceFollowRedirects(true);
      connection.setRequestMethod("HEAD");
      connection.connect();

      checkResponseCode(uri, connection);
      return new Connection(connection);
    } catch (final IOException e) {
      throw new AOException(e.getMessage(), e);
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
