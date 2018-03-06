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

package com.io7m.adoptopenjdk.v1;

import com.io7m.adoptopenjdk.spi.AOAPIRequestsType;
import com.io7m.adoptopenjdk.spi.AOException;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.adoptopenjdk.spi.AOReleases;
import com.io7m.adoptopenjdk.spi.AOVariant;

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
