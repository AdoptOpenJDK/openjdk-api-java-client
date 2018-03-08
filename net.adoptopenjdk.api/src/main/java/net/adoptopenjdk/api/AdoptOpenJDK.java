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

package net.adoptopenjdk.api;

import net.adoptopenjdk.spi.AOAPIRequestsType;
import net.adoptopenjdk.spi.AOAPIVersionProviderType;
import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;

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
    public List<AORelease> releasesForVariant(
      final String variant)
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

    @Override
    public List<AORelease> nightlyBuildsForVariant(
      final String variant)
      throws AOException
    {
      return this.requests.nightlyBuildsForVariant(variant);
    }

    @Override
    public List<AORelease> nightlyBuildsForVariantWith(
      final String variant,
      final Optional<String> os,
      final Optional<String> architecture)
      throws AOException
    {
      return this.requests.nightlyBuildsForVariantWith(variant, os, architecture);
    }
  }
}
