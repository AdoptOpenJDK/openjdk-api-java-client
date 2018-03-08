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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Functions to manipulate releases.
 */

public final class AOReleases
{
  private AOReleases()
  {

  }

  /**
   * Filter the binaries in the given release, returning a release that contains
   * only the matching binaries.
   *
   * @param release      The initial release
   * @param os           The operating system, if any
   * @param architecture The architecture, if any
   *
   * @return A filtered release
   *
   * @see AOBinaryType#hasOSAndArch(Optional, Optional)
   */

  public static AORelease releaseWithMatchingBinaries(
    final AORelease release,
    final Optional<String> os,
    final Optional<String> architecture)
  {
    Objects.requireNonNull(release, "release");
    Objects.requireNonNull(os, "os");
    Objects.requireNonNull(architecture, "architecture");

    return AORelease.builder()
      .from(release)
      .setBinaries(
        release.binaries()
          .stream()
          .filter(binary -> binary.hasOSAndArch(os, architecture))
          .collect(Collectors.toList()))
      .build();
  }

}
