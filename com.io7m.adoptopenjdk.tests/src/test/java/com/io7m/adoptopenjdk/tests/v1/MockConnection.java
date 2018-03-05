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

package com.io7m.adoptopenjdk.tests.v1;

import com.io7m.adoptopenjdk.v1.AOv1HTTPConnectionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class MockConnection implements AOv1HTTPConnectionType
{
  private final URI uri;
  private final Map<String, List<String>> headers;
  private final InputStream stream;

  MockConnection(
    final URI in_uri,
    final Map<String, List<String>> in_headers,
    final InputStream in_stream)
  {
    this.uri =
      Objects.requireNonNull(in_uri, "uri");
    this.headers =
      Objects.requireNonNull(in_headers, "headers");
    this.stream =
      Objects.requireNonNull(in_stream, "stream");

  }

  static MockConnection create(
    final Map<String, String> in_headers)
  {
    return new MockConnection(
      URI.create("anything"),
      expandHeaders(in_headers),
      new ByteArrayInputStream(new byte[0]));
  }

  static MockConnection createWithData(
    final Map<String, String> in_headers,
    final InputStream stream)
  {
    return new MockConnection(
      URI.create("anything"),
      expandHeaders(in_headers),
      stream);
  }

  private static Map<String, List<String>> expandHeaders(
    final Map<String, String> in_headers)
  {
    return Objects.requireNonNull(in_headers, "headers")
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> List.of(e.getValue())
      ));
  }

  @Override
  public InputStream input()
  {
    return this.stream;
  }

  @Override
  public Map<String, List<String>> headers()
  {
    return this.headers;
  }

  @Override
  public void close()
  {

  }
}
