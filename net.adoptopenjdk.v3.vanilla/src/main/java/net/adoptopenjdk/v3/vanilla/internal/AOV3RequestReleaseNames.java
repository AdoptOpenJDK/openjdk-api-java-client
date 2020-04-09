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
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3RequestReleaseNamesType;
import net.adoptopenjdk.v3.api.AOV3SortOrder;
import net.adoptopenjdk.v3.api.AOV3Vendor;
import net.adoptopenjdk.v3.api.AOV3VersionRange;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

final class AOV3RequestReleaseNames implements AOV3RequestReleaseNamesType
{
  private final AOV3ClientInternalType client;
  private final Consumer<AOV3Error> errorReceiver;
  private final BigInteger page;
  private final BigInteger pageSize;
  private final Optional<AOV3ReleaseKind> releaseKind;
  private final Optional<AOV3SortOrder> sortOrder;
  private final Optional<AOV3Vendor> vendor;
  private final Optional<AOV3VersionRange> versionRange;

  AOV3RequestReleaseNames(
    final AOV3ClientInternalType inClient,
    final Consumer<AOV3Error> inErrorReceiver,
    final BigInteger inPage,
    final BigInteger inPageSize,
    final Optional<AOV3ReleaseKind> inReleaseKind,
    final Optional<AOV3SortOrder> inSortOrder,
    final Optional<AOV3Vendor> inVendor,
    final Optional<AOV3VersionRange> inVersionRange)
  {
    this.client =
      Objects.requireNonNull(inClient, "client");
    this.errorReceiver =
      Objects.requireNonNull(inErrorReceiver, "errorReceiver");
    this.page =
      Objects.requireNonNull(inPage, "page");
    this.pageSize =
      Objects.requireNonNull(inPageSize, "pageSize");
    this.releaseKind =
      Objects.requireNonNull(inReleaseKind, "releaseKind");
    this.sortOrder =
      Objects.requireNonNull(inSortOrder, "sortOrder");
    this.vendor =
      Objects.requireNonNull(inVendor, "vendor");
    this.versionRange =
      Objects.requireNonNull(inVersionRange, "versionRange");
  }

  @Override
  public List<String> execute()
    throws AOV3Exception, InterruptedException
  {
    final var uriBuilder = new StringBuilder(128);
    uriBuilder.append(this.client.baseURI());
    uriBuilder.append("/info/release_names?");
    uriBuilder.append("page=");
    uriBuilder.append(this.page);
    uriBuilder.append("&page_size=");
    uriBuilder.append(this.pageSize);

    this.releaseKind.ifPresent(kind -> {
      uriBuilder.append("&release_type=");
      uriBuilder.append(kind.nameText());
    });
    this.sortOrder.ifPresent(kind -> {
      uriBuilder.append("&sort_order=");
      uriBuilder.append(kind.nameText());
    });
    this.vendor.ifPresent(kind -> {
      uriBuilder.append("&vendor=");
      uriBuilder.append(kind.nameText());
    });
    this.versionRange.ifPresent(version -> {
      uriBuilder.append("&version=");
      uriBuilder.append(URLEncoder.encode(version.toText(), UTF_8));
    });

    return this.client.parserFor(this.errorReceiver, uriBuilder.toString())
      .parseReleaseNames();
  }
}
