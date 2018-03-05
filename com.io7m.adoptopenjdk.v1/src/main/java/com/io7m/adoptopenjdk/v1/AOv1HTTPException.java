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
import java.util.Objects;

/**
 * An exception caused by a failed HTTP request.
 */

public final class AOv1HTTPException extends AOException
{
  private final int code;
  private final String server_message;
  private final URI uri;

  /**
   * Construct an exception.
   *
   * @param in_code           The HTTP error code
   * @param in_server_message The error message received from the server
   * @param in_uri            The URI
   */

  public AOv1HTTPException(
    final int in_code,
    final String in_server_message,
    final URI in_uri)
  {
    super(
      String.format(
        "%s: %d - %s",
        Objects.requireNonNull(in_uri, "uri"),
        Integer.valueOf(in_code),
        Objects.requireNonNull(in_server_message, "server_message")));
    this.code = in_code;
    this.server_message = in_server_message;
    this.uri = in_uri;
  }

  /**
   * @return The HTTP status code
   */

  public int statusCode()
  {
    return this.code;
  }

  /**
   * @return The HTTP server error message
   */

  public String serverMessage()
  {
    return this.server_message;
  }

  /**
   * @return The original URI
   */

  public URI uri()
  {
    return this.uri;
  }
}
