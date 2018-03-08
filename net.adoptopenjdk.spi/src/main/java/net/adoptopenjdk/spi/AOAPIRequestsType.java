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

import java.util.List;
import java.util.Optional;

/**
 * The type of requests that can be made to the remote server.
 */

public interface AOAPIRequestsType
{
  /**
   * @return The number of requests that can be made before the server begins rejecting requests (for rate limiting)
   */

  int rateLimitRemaining();

  /**
   * List the available build variants on the server.
   *
   * @return A list of variants
   *
   * @throws AOException On any and all errors
   */

  List<AOVariant> variants()
    throws AOException;

  /**
   * List the available releases on the server.
   *
   * @param variant The build variant
   *
   * @return A list of available releases
   *
   * @throws AOException On any and all errors
   */

  List<AORelease> releasesForVariant(
    String variant)
    throws AOException;

  /**
   * List the available releases on the server, filtering by the given
   * optional information.
   *
   * @param variant      The build variant
   * @param os           The operating system, if any
   * @param architecture The architecture, if any
   *
   * @return A list of available releases
   *
   * @throws AOException On any and all errors
   */

  List<AORelease> releasesForVariantWith(
    String variant,
    Optional<String> os,
    Optional<String> architecture)
    throws AOException;

  /**
   * List the available nightly builds on the server.
   *
   * @param variant The build variant
   *
   * @return A list of available nightly builds
   *
   * @throws AOException On any and all errors
   */

  List<AORelease> nightlyBuildsForVariant(
    String variant)
    throws AOException;

  /**
   * List the available nightly builds on the server, filtering by the given
   * optional information.
   *
   * @param variant      The build variant
   * @param os           The operating system, if any
   * @param architecture The architecture, if any
   *
   * @return A list of available nightly builds
   *
   * @throws AOException On any and all errors
   */

  List<AORelease> nightlyBuildsForVariantWith(
    String variant,
    Optional<String> os,
    Optional<String> architecture)
    throws AOException;
}
