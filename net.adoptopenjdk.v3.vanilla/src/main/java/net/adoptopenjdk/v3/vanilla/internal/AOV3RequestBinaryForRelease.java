/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com> http://io7m.com
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

package net.adoptopenjdk.v3.vanilla.internal;

import net.adoptopenjdk.v3.api.AOV3Architecture;
import net.adoptopenjdk.v3.api.AOV3Exception;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3RequestBinaryForReleaseType;
import net.adoptopenjdk.v3.api.AOV3Vendor;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

final class AOV3RequestBinaryForRelease
  implements AOV3RequestBinaryForReleaseType
{
  private final AOV3Client client;
  private final AOV3Architecture architecture;
  private final AOV3HeapSize heapSize;
  private final AOV3ImageKind imageKind;
  private final AOV3JVMImplementation jvmImplementation;
  private final AOV3OperatingSystem operatingSystem;
  private final AOV3Vendor vendor;
  private final Optional<String> project;
  private final String releaseName;

  AOV3RequestBinaryForRelease(
    final AOV3Client inClient,
    final AOV3Architecture inArchitecture,
    final AOV3HeapSize inHeapSize,
    final AOV3ImageKind inImageKind,
    final AOV3JVMImplementation inJvmImplementation,
    final AOV3OperatingSystem inOperatingSystem,
    final AOV3Vendor inVendor,
    final Optional<String> inProject,
    final String inReleaseName)
  {
    this.client =
      Objects.requireNonNull(inClient, "inClient");
    this.architecture =
      Objects.requireNonNull(inArchitecture, "inArchitecture");
    this.heapSize =
      Objects.requireNonNull(inHeapSize, "inHeapSize");
    this.imageKind =
      Objects.requireNonNull(inImageKind, "inImageKind");
    this.jvmImplementation =
      Objects.requireNonNull(inJvmImplementation, "inJvmImplementation");
    this.operatingSystem =
      Objects.requireNonNull(inOperatingSystem, "inOperatingSystem");
    this.vendor =
      Objects.requireNonNull(inVendor, "inVendor");
    this.project =
      Objects.requireNonNull(inProject, "inProject");
    this.releaseName =
      Objects.requireNonNull(inReleaseName, "releaseName");
  }

  @Override
  public URI execute()
    throws AOV3Exception, InterruptedException
  {
    final var uriBuilder = new StringBuilder(128);
    uriBuilder.append(this.client.baseURI());
    uriBuilder.append("/binary/version/");
    uriBuilder.append(this.releaseName);
    uriBuilder.append("/");
    uriBuilder.append(this.operatingSystem.nameText());
    uriBuilder.append("/");
    uriBuilder.append(this.architecture.nameText());
    uriBuilder.append("/");
    uriBuilder.append(this.imageKind.nameText());
    uriBuilder.append("/");
    uriBuilder.append(this.jvmImplementation.nameText());
    uriBuilder.append("/");
    uriBuilder.append(this.heapSize.nameText());
    uriBuilder.append("/");
    uriBuilder.append(this.vendor.nameText());

    this.project.ifPresent(name -> {
      uriBuilder.append("?project=");
      uriBuilder.append(name);
    });

    return this.client.uriFor(uriBuilder.toString());
  }
}
