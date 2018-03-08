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

package net.adoptopenjdk.spi;

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
