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

import net.adoptopenjdk.v3.api.AOV3Architecture;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3Exception;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3Release;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3RequestAssetsForReleaseType;
import net.adoptopenjdk.v3.api.AOV3SortOrder;
import net.adoptopenjdk.v3.api.AOV3Vendor;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final class AOV3RequestAssetsForRelease
  implements AOV3RequestAssetsForReleaseType
{
  private final AOV3ClientInternalType client;
  private final Consumer<AOV3Error> errorReceiver;
  private final BigInteger page;
  private final BigInteger pageSize;
  private final BigInteger version;
  private final AOV3ReleaseKind releaseKind;
  private final Optional<AOV3Architecture> architecture;
  private final Optional<AOV3HeapSize> heapSize;
  private final Optional<AOV3ImageKind> imageKind;
  private final Optional<AOV3JVMImplementation> jvmImplementation;
  private final Optional<AOV3OperatingSystem> operatingSystem;
  private final Optional<String> project;
  private final Optional<AOV3SortOrder> sortOrder;
  private final Optional<AOV3Vendor> vendor;

  // CHECKSTYLE:OFF
  AOV3RequestAssetsForRelease(
    final AOV3ClientInternalType inClient,
    final Consumer<AOV3Error> inErrorReceiver,
    final BigInteger inPage,
    final BigInteger inPageSize,
    final BigInteger inVersion,
    final AOV3ReleaseKind inReleaseKind,
    final Optional<AOV3Architecture> inArchitecture,
    final Optional<AOV3HeapSize> inHeapSize,
    final Optional<AOV3ImageKind> inImageKind,
    final Optional<AOV3JVMImplementation> inJvmImplementation,
    final Optional<AOV3OperatingSystem> inOperatingSystem,
    final Optional<String> inProject,
    final Optional<AOV3SortOrder> inSortOrder,
    final Optional<AOV3Vendor> inVendor
    // CHECKSTYLE:ON
  )
  {
    this.client =
      Objects.requireNonNull(inClient, "client");
    this.errorReceiver =
      Objects.requireNonNull(inErrorReceiver, "errorReceiver");
    this.page =
      Objects.requireNonNull(inPage, "page");
    this.pageSize =
      Objects.requireNonNull(inPageSize, "pageSize");
    this.version =
      Objects.requireNonNull(inVersion, "version");
    this.releaseKind =
      Objects.requireNonNull(inReleaseKind, "releaseKind");
    this.architecture =
      Objects.requireNonNull(inArchitecture, "architecture");
    this.heapSize =
      Objects.requireNonNull(inHeapSize, "heapSize");
    this.imageKind =
      Objects.requireNonNull(inImageKind, "imageKind");
    this.jvmImplementation =
      Objects.requireNonNull(inJvmImplementation, "jvmImplementation");
    this.operatingSystem =
      Objects.requireNonNull(inOperatingSystem, "operatingSystem");
    this.project =
      Objects.requireNonNull(inProject, "project");
    this.sortOrder =
      Objects.requireNonNull(inSortOrder, "sortOrder");
    this.vendor =
      Objects.requireNonNull(inVendor, "vendor");
  }

  @Override
  public List<AOV3Release> execute()
    throws AOV3Exception, InterruptedException
  {
    final var uriBuilder = new StringBuilder(128);
    uriBuilder.append(this.client.baseURI());
    uriBuilder.append("/assets/feature_releases/");
    uriBuilder.append(this.version);
    uriBuilder.append("/");
    uriBuilder.append(this.releaseKind.nameText());

    uriBuilder.append("?page=");
    uriBuilder.append(this.page);
    uriBuilder.append("&page_size=");
    uriBuilder.append(this.pageSize);

    this.architecture.ifPresent(kind -> {
      uriBuilder.append("&architecture=");
      uriBuilder.append(kind.nameText());
    });
    this.heapSize.ifPresent(kind -> {
      uriBuilder.append("&heap_size=");
      uriBuilder.append(kind.nameText());
    });
    this.imageKind.ifPresent(kind -> {
      uriBuilder.append("&image_type=");
      uriBuilder.append(kind.nameText());
    });
    this.jvmImplementation.ifPresent(kind -> {
      uriBuilder.append("&jvm_impl=");
      uriBuilder.append(kind.nameText());
    });
    this.operatingSystem.ifPresent(kind -> {
      uriBuilder.append("&os=");
      uriBuilder.append(kind.nameText());
    });
    this.project.ifPresent(kind -> {
      uriBuilder.append("&project=");
      uriBuilder.append(kind);
    });
    this.sortOrder.ifPresent(kind -> {
      uriBuilder.append("&sort_order=");
      uriBuilder.append(kind.nameText());
    });
    this.vendor.ifPresent(kind -> {
      uriBuilder.append("&vendor=");
      uriBuilder.append(kind.nameText());
    });

    return this.client.parserFor(this.errorReceiver, uriBuilder.toString())
      .parseAssetsForRelease();
  }
}
