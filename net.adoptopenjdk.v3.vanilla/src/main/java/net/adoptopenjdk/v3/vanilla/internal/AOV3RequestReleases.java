/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com>
 *
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

package net.adoptopenjdk.v3.vanilla.internal;

import net.adoptopenjdk.v3.api.AOV3AvailableReleases;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3Exception;
import net.adoptopenjdk.v3.api.AOV3RequestReleasesType;

import java.util.Objects;
import java.util.function.Consumer;

final class AOV3RequestReleases implements AOV3RequestReleasesType
{
  private final AOV3ClientInternalType client;
  private final Consumer<AOV3Error> errorReceiver;

  AOV3RequestReleases(
    final Consumer<AOV3Error> inErrorReceiver,
    final AOV3ClientInternalType inClient)
  {
    this.errorReceiver =
      Objects.requireNonNull(inErrorReceiver, "inErrorReceiver");
    this.client =
      Objects.requireNonNull(inClient, "client");
  }

  @Override
  public AOV3AvailableReleases execute()
    throws AOV3Exception, InterruptedException
  {
    final var uriBuilder = new StringBuilder(128);
    uriBuilder.append(this.client.baseURI());
    uriBuilder.append("/info/available_releases");

    return this.client.parserFor(this.errorReceiver, uriBuilder.toString())
      .parseAvailableReleases();
  }
}
