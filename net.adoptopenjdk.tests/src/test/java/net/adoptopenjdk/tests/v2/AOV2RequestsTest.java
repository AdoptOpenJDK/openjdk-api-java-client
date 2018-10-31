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

package net.adoptopenjdk.tests.v2;

import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;
import net.adoptopenjdk.tests.spi.AOReleasesTest;
import net.adoptopenjdk.v2.AOv2Requests;
import net.adoptopenjdk.v2.AOv2RequestsType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class AOV2RequestsTest
{
  private static InputStream resource(
    final String name)
    throws IOException
  {
    final URL url = AOv2ParserTest.class.getResource(name);
    if (url == null) {
      throw new NoSuchFileException("No such resource: " + name);
    }
    return url.openStream();
  }

  @Test
  public void testOpenNonsenseRateLimit()
    throws AOException
  {
    final MockConnection connection_0 =
      MockConnection.create(
        Map.of("X-RateLimit-Remaining", "xyz",
               "Retry-After", "3600"));

    final MockConnections connections = new MockConnections();
    connections.addConnection(connection_0);

    final AOv2RequestsType requests = AOv2Requests.open(connections);
    Assert.assertEquals((long) Integer.MAX_VALUE, (long) requests.rateLimitRemaining());
    Assert.assertEquals(0L, (long) connections.queue().size());
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

    final AOv2RequestsType requests = AOv2Requests.open(connections);
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

    final AOv2RequestsType requests = AOv2Requests.open(connections);
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

    final AOv2RequestsType requests = AOv2Requests.open(connections);
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

    final AOv2RequestsType requests = AOv2Requests.open(connections);
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