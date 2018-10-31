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

import net.adoptopenjdk.spi.AOParseException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;
import net.adoptopenjdk.tests.spi.AOReleasesTest;
import net.adoptopenjdk.v2.AOv2Parser;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.List;

public final class AOv2ParserTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOv2ParserTest.class);
  
  @Rule public ExpectedException expected = ExpectedException.none();

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
  public void testOpenJDK8Releases()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8.json")) {
      final List<AORelease> releases =
        parser.parseReleases(URI.create("openjdk8.json"), stream);

      Assert.assertEquals(7L, (long) releases.size());

      final AORelease release_0 = AOReleasesTest.firstJDK8Release();
      Assert.assertEquals(release_0, releases.get(0));
    }
  }

  @Test
  public void testOpenJDK8ReleasesWarnings()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_warnings.json")) {
      final List<AORelease> releases =
        parser.parseReleases(URI.create("openjdk8_warnings.json"), stream);

      Assert.assertEquals(1L, (long) releases.size());
      final AORelease release_0 = AOReleasesTest.firstJDK8Release();
      Assert.assertEquals(release_0, releases.get(0));
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad0()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_0.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad1()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_1.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad2()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_2.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad3()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_3.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad4()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_4.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testOpenJDK8Releases_Bad5()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("openjdk8_bad_5.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseReleases(URI.create("openjdk8.json"), stream);
    }
  }

  @Test
  public void testVariants()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("variants.json")) {
      final List<AOVariant> variants =
        parser.parseVariants(URI.create("variants.json"), stream);

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

  @Test
  public void testVariantsWarnings()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("variants_warnings.json")) {
      final List<AOVariant> variants =
        parser.parseVariants(URI.create("variants_warnings.json"), stream);

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

  @Test
  public void testVariants_Bad0()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("variants_bad_0.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseVariants(URI.create("variants_bad_0.json"), stream);
    }
  }

  @Test
  public void testVariants_Bad1()
    throws Exception
  {
    final AOv2Parser parser = new AOv2Parser();

    try (InputStream stream = resource("variants_bad_1.json")) {
      this.expected.expect(AOParseException.class);
      parser.parseVariants(URI.create("variants_bad_1.json"), stream);
    }
  }
}
