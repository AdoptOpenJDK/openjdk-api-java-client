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
