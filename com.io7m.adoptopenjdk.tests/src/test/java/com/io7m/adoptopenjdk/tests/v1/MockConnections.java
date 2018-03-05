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
import com.io7m.adoptopenjdk.v1.AOv1HTTPConnectionsType;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

final class MockConnections implements AOv1HTTPConnectionsType
{
  private final Queue<MockConnection> queue;

  MockConnections()
  {
    this.queue = new ArrayDeque<>();
  }

  public Queue<MockConnection> queue()
  {
    return this.queue;
  }

  @Override
  public AOv1HTTPConnectionType head(
    final URI uri,
    final Map<String, String> headers)
  {
    return this.queue.remove();
  }

  @Override
  public AOv1HTTPConnectionType get(
    final URI uri,
    final Map<String, String> headers)
  {
    return this.queue.remove();
  }

  public void addConnection(final MockConnection connection)
  {
    this.queue.add(connection);
  }
}
