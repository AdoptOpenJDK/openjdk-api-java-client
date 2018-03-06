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

package com.io7m.adoptopenjdk.api;

import com.io7m.adoptopenjdk.spi.AOAPIRequestsType;
import com.io7m.adoptopenjdk.spi.AOAPIVersionProviderType;
import com.io7m.adoptopenjdk.spi.AOException;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.adoptopenjdk.spi.AOVariant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * The AdoptOpenJDK API.
 */

public final class AdoptOpenJDK
{
  private AdoptOpenJDK()
  {

  }

  /**
   * Find a provider for the API with the given major version.
   *
   * @param version The major version
   *
   * @return A requests interface
   *
   * @throws AOException               On initialization errors
   * @throws ServiceConfigurationError If no provider is available supporting the given version
   */

  public static AdoptOpenJDKRequestsType get(final int version)
    throws AOException, ServiceConfigurationError
  {
    final ServiceLoader<AOAPIVersionProviderType> loader =
      ServiceLoader.load(AOAPIVersionProviderType.class);

    final Optional<AOAPIVersionProviderType> result =
      loader.stream()
        .map(ServiceLoader.Provider::get)
        .filter(provider -> provider.supportedMajorAPI() == version)
        .findFirst();

    if (!result.isPresent()) {
      throw new ServiceConfigurationError("No service provider is available");
    }

    final AOAPIRequestsType provider = result.get().create();
    return wrap(provider);
  }

  /**
   * Wrap a service provider to conform to the {@link AdoptOpenJDKRequestsType} API.
   *
   * @param provider The provider
   *
   * @return A requests API
   */

  public static AdoptOpenJDKRequestsType wrap(
    final AOAPIRequestsType provider)
  {
    return new Requests(Objects.requireNonNull(provider, "provider"));
  }

  private static final class Requests implements AdoptOpenJDKRequestsType
  {
    private final AOAPIRequestsType requests;

    Requests(final AOAPIRequestsType in_requests)
    {
      this.requests = Objects.requireNonNull(in_requests, "Requests");
    }

    @Override
    public int rateLimitRemaining()
    {
      return this.requests.rateLimitRemaining();
    }

    @Override
    public List<AOVariant> variants()
      throws AOException
    {
      return this.requests.variants();
    }

    @Override
    public List<AORelease> releasesForVariant(final String variant)
      throws AOException
    {
      return this.requests.releasesForVariant(variant);
    }

    @Override
    public List<AORelease> releasesForVariantWith(
      final String variant,
      final Optional<String> os,
      final Optional<String> architecture)
      throws AOException
    {
      return this.requests.releasesForVariantWith(variant, os, architecture);
    }
  }
}
