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

import com.io7m.adoptopenjdk.spi.AOException;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.adoptopenjdk.spi.AOVariant;
import com.io7m.adoptopenjdk.tests.spi.AOReleasesTest;
import com.io7m.adoptopenjdk.v1.AOv1Requests;
import com.io7m.adoptopenjdk.v1.AOv1RequestsType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class AOV1RequestsTest
{
  private static InputStream resource(
    final String name)
    throws IOException
  {
    final URL url = AOv1ParserTest.class.getResource(name);
    if (url == null) {
      throw new NoSuchFileException("No such resource: " + name);
    }
    return url.openStream();
  }

  @Test
  public void testOpen()
    throws AOException
  {
    final MockConnection connection_0 =
      MockConnection.create(
        Map.of("X-RateLimit-Remaining", "100",
               "Retry-After", "3600"));

    final MockConnections connections = new MockConnections();
    connections.addConnection(connection_0);

    final AOv1RequestsType requests = AOv1Requests.open(connections);
    Assert.assertEquals(100L, (long) requests.rateLimitRemaining());
    Assert.assertEquals(0L, (long) connections.queue().size());
  }

  @Test
  public void testParseOpenJDK8Releases()
    throws AOException, IOException
  {
    final MockConnection connection_0 =
      MockConnection.create(
        Map.of("X-RateLimit-Remaining", "100",
               "Retry-After", "3600"));

    final MockConnection connection_1 =
      MockConnection.createWithData(
        Map.of("X-RateLimit-Remaining", "200",
               "Retry-After", "3600"),
        resource("openjdk8.json"));

    final MockConnections connections = new MockConnections();
    connections.addConnection(connection_0);
    connections.addConnection(connection_1);

    final AOv1RequestsType requests = AOv1Requests.open(connections);
    final List<AORelease> releases = requests.releasesForVariant("openjdk8");

    Assert.assertEquals(200L, (long) requests.rateLimitRemaining());
    Assert.assertEquals(0L, (long) connections.queue().size());
    Assert.assertEquals(7L, (long) releases.size());
    Assert.assertEquals(AOReleasesTest.firstJDK8Release(), releases.get(0));
  }

  @Test
  public void testParseOpenJDK8ReleasesFiltered()
    throws AOException, IOException
  {
    final MockConnection connection_0 =
      MockConnection.create(
        Map.of("X-RateLimit-Remaining", "100",
               "Retry-After", "3600"));

    final MockConnection connection_1 =
      MockConnection.createWithData(
        Map.of("X-RateLimit-Remaining", "200",
               "Retry-After", "3600"),
        resource("openjdk8.json"));

    final MockConnection connection_2 =
      MockConnection.createWithData(
        Map.of("X-RateLimit-Remaining", "200",
               "Retry-After", "3600"),
        resource("openjdk8.json"));

    final MockConnections connections = new MockConnections();
    connections.addConnection(connection_0);
    connections.addConnection(connection_1);
    connections.addConnection(connection_2);

    /*
     * Making a request, and then making the same request with filtering
     * where no actual filters are specified, should return identical results.
     */

    final AOv1RequestsType requests = AOv1Requests.open(connections);
    final List<AORelease> releases_0 =
      requests.releasesForVariant("openjdk8");
    final List<AORelease> releases_1 =
      requests.releasesForVariantWith(
        "openjdk8",
        Optional.empty(),
        Optional.empty());

    Assert.assertEquals(releases_0, releases_1);
  }

  @Test
  public void testParseVariants()
    throws AOException, IOException
  {
    final MockConnection connection_0 =
      MockConnection.create(
        Map.of("X-RateLimit-Remaining", "100",
               "Retry-After", "3600"));

    final MockConnection connection_1 =
      MockConnection.createWithData(
        Map.of("X-RateLimit-Remaining", "200",
               "Retry-After", "3600"),
        resource("variants.json"));

    final MockConnections connections = new MockConnections();
    connections.addConnection(connection_0);
    connections.addConnection(connection_1);

    final AOv1RequestsType requests = AOv1Requests.open(connections);
    final List<AOVariant> variants = requests.variants();

    Assert.assertEquals(200L, (long) requests.rateLimitRemaining());
    Assert.assertEquals(0L, (long) connections.queue().size());
    Assert.assertEquals(5L, (long) variants.size());

    Assert.assertEquals(
      AOVariant.of("openjdk8", "OpenJDK 8 with Hotspot"),
      variants.get(0));
    Assert.assertEquals(
      AOVariant.of("openjdk8-openj9", "OpenJDK 8 with Eclipse OpenJ9"),
      variants.get(1));
    Assert.assertEquals(
      AOVariant.of("openjdk9", "OpenJDK 9 with Hotspot"),
      variants.get(2));
    Assert.assertEquals(
      AOVariant.of("openjdk9-openj9", "OpenJDK 9 with Eclipse OpenJ9"),
      variants.get(3));
    Assert.assertEquals(
      AOVariant.of("openjdk10", "OpenJDK 10 with Hotspot"),
      variants.get(4));
  }
}
