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

package net.adoptopenjdk.v1;

import net.adoptopenjdk.spi.AOAPIRequestsType;
import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOReleases;
import net.adoptopenjdk.spi.AOVariant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type of requests that can be made to the remote server.
 */

public interface AOv1RequestsType extends AOAPIRequestsType
{
  @Override
  int rateLimitRemaining();

  @Override
  List<AOVariant> variants()
    throws AOException;

  @Override
  List<AORelease> releasesForVariant(
    String variant)
    throws AOException;

  @Override
  default List<AORelease> releasesForVariantWith(
    final String variant,
    final Optional<String> os,
    final Optional<String> architecture)
    throws AOException
  {
    Objects.requireNonNull(variant, "variant");
    Objects.requireNonNull(os, "operating_system");
    Objects.requireNonNull(architecture, "architecture");

    return this.releasesForVariant(variant)
      .stream()
      .filter(r -> r.hasBinaryWith(os, architecture))
      .map(r -> AOReleases.releaseWithMatchingBinaries(r, os, architecture))
      .collect(Collectors.toList());
  }

  @Override
  List<AORelease> nightlyBuildsForVariant(
    String variant)
    throws AOException;

  @Override
  default List<AORelease> nightlyBuildsForVariantWith(
    final String variant,
    final Optional<String> os,
    final Optional<String> architecture)
    throws AOException
  {
    Objects.requireNonNull(variant, "variant");
    Objects.requireNonNull(os, "operating_system");
    Objects.requireNonNull(architecture, "architecture");

    return this.nightlyBuildsForVariant(variant)
      .stream()
      .filter(r -> r.hasBinaryWith(os, architecture))
      .map(r -> AOReleases.releaseWithMatchingBinaries(r, os, architecture))
      .collect(Collectors.toList());
  }
}
