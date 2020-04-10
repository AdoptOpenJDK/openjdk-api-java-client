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

import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3Exception;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3ListBinaryAssetView;
import net.adoptopenjdk.v3.api.AOV3RequestAssetsForLatestType;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class AOV3RequestAssetsForLatest
  implements AOV3RequestAssetsForLatestType
{
  private final AOV3ClientInternalType client;
  private final AOV3JVMImplementation jvmImplementation;
  private final BigInteger version;
  private final Consumer<AOV3Error> errorReceiver;

  AOV3RequestAssetsForLatest(
    final AOV3ClientInternalType inClient,
    final Consumer<AOV3Error> inErrorReceiver,
    final BigInteger inVersion,
    final AOV3JVMImplementation inJvmImplementation)
  {
    this.client =
      Objects.requireNonNull(inClient, "inClient");
    this.errorReceiver =
      Objects.requireNonNull(inErrorReceiver, "errorReceiver");
    this.version =
      Objects.requireNonNull(inVersion, "version");
    this.jvmImplementation =
      Objects.requireNonNull(inJvmImplementation, "jvmImplementation");
  }

  @Override
  public List<AOV3ListBinaryAssetView> execute()
    throws AOV3Exception, InterruptedException
  {
    final var uriBuilder = new StringBuilder(128);
    uriBuilder.append(this.client.baseURI());
    uriBuilder.append("/assets/latest/");
    uriBuilder.append(this.version);
    uriBuilder.append("/");
    uriBuilder.append(this.jvmImplementation.nameText());

    return this.client.parserFor(this.errorReceiver, uriBuilder.toString())
      .parseAssetsForLatest();
  }
}
