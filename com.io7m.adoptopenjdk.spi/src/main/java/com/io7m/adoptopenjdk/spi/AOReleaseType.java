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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A specific release.
 */

@ImmutableStyleType
@Value.Immutable
public interface AOReleaseType
{
  /**
   * @return The name of the release (such as {@code jdk-10+23}
   */

  @Value.Parameter
  String name();

  /**
   * @return The date and time that the release was made available
   */

  @Value.Parameter
  ZonedDateTime time();

  /**
   * @return The available binaries
   */

  @Value.Parameter
  List<AOBinary> binaries();

  /**
   * @param operating_system The OS, if any
   * @param architecture     The architecture, if any
   *
   * @return {@code true} iff this release has any binaries with the given operating system and arch
   *
   * @see AOBinaryType#hasOSAndArch(Optional, Optional)
   */

  default boolean hasBinaryWith(
    final Optional<String> operating_system,
    final Optional<String> architecture)
  {
    Objects.requireNonNull(operating_system, "operating system");
    Objects.requireNonNull(architecture, "architecture");

    return this.binaries()
      .stream()
      .anyMatch(binary -> binary.hasOSAndArch(operating_system, architecture));
  }
}
