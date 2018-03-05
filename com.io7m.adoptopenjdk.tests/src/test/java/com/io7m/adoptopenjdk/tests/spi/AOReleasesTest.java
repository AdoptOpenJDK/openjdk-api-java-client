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

package com.io7m.adoptopenjdk.tests.spi;

import com.io7m.adoptopenjdk.spi.AOBinary;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.adoptopenjdk.spi.AOReleases;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public final class AOReleasesTest
{
  public static AORelease firstJDK8Release()
  {
    return AORelease.builder()
      .setName("jdk8u144-b01")
      .setTime(ZonedDateTime.parse("2017-07-27T22:01:53Z"))
      .setBinaries(List.of(
        AOBinary.builder()
          .setOperatingSystem("Linux")
          .setArchitecture("aarch64")
          .setName("OpenJDK8_aarch64_Linux_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_aarch64_Linux_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_aarch64_Linux_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("72000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("Linux")
          .setArchitecture("ppc64le")
          .setName("OpenJDK8_ppc64le_Linux_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_ppc64le_Linux_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_ppc64le_Linux_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("72000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("AIX")
          .setArchitecture("ppc64")
          .setName("OpenJDK8_ppc64_AIX_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_ppc64_AIX_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_ppc64_AIX_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("76000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("Linux")
          .setArchitecture("s390x")
          .setName("OpenJDK8_s390x_Linux_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_s390x_Linux_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_s390x_Linux_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("70000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("Linux")
          .setArchitecture("x64")
          .setName("OpenJDK8_x64_Linux_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Linux_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Linux_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("75000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("macOS")
          .setArchitecture("x64")
          .setName("OpenJDK8_x64_Mac_jdk8u144-b01.tar.gz")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Mac_jdk8u144-b01.tar.gz"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Mac_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("72000000"))
          .build(),
        AOBinary.builder()
          .setOperatingSystem("Windows")
          .setArchitecture("x64")
          .setName("OpenJDK8_x64_Win_jdk8u144-b01.zip")
          .setLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Win_jdk8u144-b01.zip"))
          .setChecksumLink(URI.create(
            "https://github.com/AdoptOpenJDK/openjdk8-releases/releases/download/jdk8u144-b01/OpenJDK8_x64_Win_jdk8u144-b01.sha256.txt"))
          .setSize(new BigInteger("73000000"))
          .build()))
      .build();
  }

  @Test
  public void testIdentity()
  {
    final AORelease source = firstJDK8Release();
    Assert.assertEquals(
      source,
      AOReleases.releaseWithMatchingBinaries(
        source,
        Optional.empty(),
        Optional.empty()));
  }

  @Test
  public void testWindowsOnly()
  {
    final AORelease source = firstJDK8Release();
    final AORelease release =
      AOReleases.releaseWithMatchingBinaries(
        source,
        Optional.of("Windows"),
        Optional.empty());

    Assert.assertEquals(
      source.binaries().get(6),
      release.binaries().get(0));
  }

  @Test
  public void testX64Only()
  {
    final AORelease source = firstJDK8Release();
    final AORelease release =
      AOReleases.releaseWithMatchingBinaries(
        source,
        Optional.empty(),
        Optional.of("x64"));

    Assert.assertEquals(
      source.binaries().get(4),
      release.binaries().get(0));
    Assert.assertEquals(
      source.binaries().get(5),
      release.binaries().get(1));
    Assert.assertEquals(
      source.binaries().get(6),
      release.binaries().get(2));
  }
}
