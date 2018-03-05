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

package com.io7m.adoptopenjdk.spi;

import org.immutables.value.Value;

import java.math.BigInteger;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * An available binary.
 */

@ImmutableStyleType
@Value.Immutable
public interface AOBinaryType
{
  /**
   * @return The (lowercase) name of the operating system
   */

  @Value.Parameter
  String operatingSystem();

  /**
   * @return The (lowercase) name of the hardware architecture
   */

  @Value.Parameter
  String architecture();

  /**
   * @return The filename of the binary (such as {@code OpenJDK10_x64_Linux_jdk-10.23.tar.gz}
   */

  @Value.Parameter
  String name();

  /**
   * @return A link to the binary for downloads
   */

  @Value.Parameter
  URI link();

  /**
   * @return The size in octets of the binary
   */

  @Value.Parameter
  BigInteger size();

  /**
   * @return A link to a file containing the checksum of the binary (typically SHA256)
   */

  @Value.Parameter
  URI checksumLink();

  /**
   * Determine if the current binary has the given operating system and architecture.
   * If no operating system or architecture is provided, the function returns {@code true}
   * for either check. The check is case insensitive.
   *
   * @param operating_system The operating system against which to match, if any
   * @param architecture     The architecture against which to match, if any
   *
   * @return {@code true} if this binary has both of the given properties
   */

  default boolean hasOSAndArch(
    final Optional<String> operating_system,
    final Optional<String> architecture)
  {
    Objects.requireNonNull(operating_system, "operating_system");
    Objects.requireNonNull(architecture, "architecture");

    final Boolean os_match =
      operating_system.map(
        os -> Boolean.valueOf(this.operatingSystem().equalsIgnoreCase(os)))
        .orElse(Boolean.TRUE);
    final Boolean arch_match =
      architecture.map(
        arch -> Boolean.valueOf(this.architecture().equalsIgnoreCase(arch)))
        .orElse(Boolean.TRUE);

    return os_match.booleanValue() && arch_match.booleanValue();
  }
}
