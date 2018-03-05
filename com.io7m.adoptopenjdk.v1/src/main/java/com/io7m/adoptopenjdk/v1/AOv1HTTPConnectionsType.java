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

import java.net.URI;
import java.util.Map;

/**
 * A provider of HTTP connections.
 */

public interface AOv1HTTPConnectionsType
{
  /**
   * Send a HEAD request to the given URI.
   *
   * @param uri     The URI
   * @param headers The client headers
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  AOv1HTTPConnectionType head(
    URI uri,
    Map<String, String> headers)
    throws AOException, AOv1HTTPException;

  /**
   * Send a HEAD request to the given URI.
   *
   * @param uri The URI
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  default AOv1HTTPConnectionType head(
    final URI uri)
    throws AOException, AOv1HTTPException
  {
    return this.head(uri, Map.of());
  }

  /**
   * Send a GET request to the given URI.
   *
   * @param uri     The URI
   * @param headers The client headers
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  AOv1HTTPConnectionType get(
    URI uri,
    Map<String, String> headers)
    throws AOException, AOv1HTTPException;

  /**
   * Send a GET request to the given URI.
   *
   * @param uri The URI
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  default AOv1HTTPConnectionType get(
    final URI uri)
    throws AOException, AOv1HTTPException
  {
    return this.get(uri, Map.of());
  }
}
