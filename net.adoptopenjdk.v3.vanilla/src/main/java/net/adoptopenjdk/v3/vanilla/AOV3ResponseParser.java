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

package net.adoptopenjdk.v3.vanilla;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.adoptopenjdk.v3.api.AOV3Architecture;
import net.adoptopenjdk.v3.api.AOV3AvailableReleases;
import net.adoptopenjdk.v3.api.AOV3Binary;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3Installer;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3Package;
import net.adoptopenjdk.v3.api.AOV3Release;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3Source;
import net.adoptopenjdk.v3.api.AOV3Vendor;
import net.adoptopenjdk.v3.api.AOV3VersionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AOV3ResponseParser implements AOV3ResponseParserType
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV3ResponseParser.class);

  private final URI source;
  private final InputStream stream;
  private final Consumer<AOV3Error> errorReceiver;
  private final ObjectMapper objectMapper;

  public AOV3ResponseParser(
    final Consumer<AOV3Error> inErrorReceiver,
    final ObjectMapper inObjectMapper,
    final URI inSource,
    final InputStream inStream)
  {
    this.errorReceiver =
      Objects.requireNonNull(inErrorReceiver, "errorReceiver");
    this.objectMapper =
      Objects.requireNonNull(inObjectMapper, "objectMapper");
    this.source =
      Objects.requireNonNull(inSource, "source");
    this.stream =
      Objects.requireNonNull(inStream, "stream");
  }

  private static AOV3Binary toBinary(
    final AOV3AST.AOV3BinaryJSON binary)
  {
    final var architecture =
      AOV3Architecture.of(
        Objects.requireNonNull(
          binary.architecture, "binary.architecture")
      );

    final var downloadCount =
      Objects.requireNonNull(
        binary.downloadCount, "binary.downloadCount");

    final var heapSize =
      AOV3HeapSize.of(
        Objects.requireNonNull(
          binary.heapSize, "binary.heapSize")
      );

    final var imageType =
      AOV3ImageKind.of(
        Objects.requireNonNull(
          binary.imageType, "binary.imageType")
      );

    final var jvmImplementation =
      AOV3JVMImplementation.of(
        Objects.requireNonNull(
          binary.jvmImplementation, "binary.jvmImplementation")
      );

    final var operatingSystem =
      AOV3OperatingSystem.of(
        Objects.requireNonNull(
          binary.operatingSystem, "binary.operatingSystem")
      );

    final var installer =
      Optional.ofNullable(binary.installer)
        .map(AOV3ResponseParser::toInstaller);

    final var scmReference =
      Optional.ofNullable(binary.scmReference);

    return AOV3Binary.builder()
      .setArchitecture(architecture)
      .setDownloadCount(downloadCount)
      .setHeapSize(heapSize)
      .setImageType(imageType)
      .setInstaller(installer)
      .setJvmImplementation(jvmImplementation)
      .setOperatingSystem(operatingSystem)
      .setPackage_(toPackage(binary.package_))
      .setProject(binary.project)
      .setScmReference(scmReference)
      .setUpdatedAt(toOffsetDateTime(binary.updatedAt))
      .build();
  }

  private static AOV3Installer toInstaller(
    final AOV3AST.AOV3InstallerJSON installer)
  {
    final var checksum =
      Objects.requireNonNull(installer.checksum, "installer.checksum");
    final var checksumLink =
      Optional.ofNullable(installer.checksumLink);
    final var downloadCount =
      Objects.requireNonNull(
        installer.downloadCount,
        "installer.downloadCount");
    final var link =
      Objects.requireNonNull(installer.link, "installer.link");
    final var name =
      Objects.requireNonNull(installer.name, "installer.name");
    final var signatureLink =
      Optional.ofNullable(installer.signatureLink);
    final var size =
      Objects.requireNonNull(installer.size, "installer.size");

    return AOV3Installer.builder()
      .setChecksum(checksum)
      .setChecksumLink(checksumLink)
      .setDownloadCount(downloadCount)
      .setLink(link)
      .setName(name)
      .setSignatureLink(signatureLink)
      .setSize(size)
      .build();
  }

  private static AOV3Package toPackage(
    final AOV3AST.AOV3PackageJSON package_)
  {
    final var checksum =
      Objects.requireNonNull(package_.checksum, "package_.checksum");
    final var checksumLink =
      Optional.ofNullable(package_.checksumLink);
    final var downloadCount =
      Objects.requireNonNull(package_.downloadCount, "package_.downloadCount");
    final var link =
      Objects.requireNonNull(package_.link, "package_.link");
    final var name =
      Objects.requireNonNull(package_.name, "package_.name");
    final var signatureLink =
      Optional.ofNullable(package_.signatureLink);
    final var size =
      Objects.requireNonNull(package_.size, "package_.size");

    return AOV3Package.builder()
      .setChecksum(checksum)
      .setChecksumLink(checksumLink)
      .setDownloadCount(downloadCount)
      .setLink(link)
      .setName(name)
      .setSignatureLink(signatureLink)
      .setSize(size)
      .build();
  }

  private static AOV3VersionData toVersionData(
    final AOV3AST.AOV3ReleaseVersionJSON versionData)
  {
    return AOV3VersionData.builder()
      .setAdoptBuildNumber(versionData.adoptBuildNumber)
      .setBuild(versionData.build)
      .setMajor(versionData.major)
      .setMinor(versionData.minor)
      .setOpenJDKVersion(versionData.openjdkVersion)
      .setOptional(versionData.optional)
      .setPre(versionData.pre)
      .setSecurity(versionData.security)
      .setSemanticVersion(versionData.semver)
      .build();
  }

  private static AOV3Vendor toVendor(
    final String vendor)
  {
    return AOV3Vendor.of(Objects.requireNonNull(vendor, "vendor"));
  }

  private static OffsetDateTime toOffsetDateTime(
    final String text)
  {
    return OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  private static AOV3Source toSource(
    final AOV3AST.AOV3SourceJSON source)
  {
    return AOV3Source.builder()
      .setLink(source.link)
      .setName(source.name)
      .setSize(source.size)
      .build();
  }

  private AOV3Release toRelease(
    final AOV3AST.AOV3ReleaseJSON release)
  {
    final var builder = AOV3Release.builder();

    final var downloadCount =
      Objects.requireNonNull(release.downloadCount, "release.downloadCount");
    final var id =
      Objects.requireNonNull(release.id, "release.id");
    final var releaseLink =
      Objects.requireNonNull(release.releaseLink, "release.releaseLink");
    final var releaseName =
      Objects.requireNonNull(release.releaseName, "release.releaseName");
    final var of =
      AOV3ReleaseKind.of(
        Objects.requireNonNull(release.releaseType, "release.releaseType"));
    final var timestamp =
      toOffsetDateTime(
        Objects.requireNonNull(release.timestamp, "release.timestamp"));
    final var updatedAt =
      toOffsetDateTime(
        Objects.requireNonNull(release.updatedAt, "release.updatedAt"));
    final var vendor =
      toVendor(
        Objects.requireNonNull(release.vendor, "release.vendor"));
    final var versionData =
      toVersionData(
        Objects.requireNonNull(release.versionData, "release.versionData"));
    final var source =
      Optional.ofNullable(release.source).map(AOV3ResponseParser::toSource);

    builder
      .setDownloadCount(downloadCount)
      .setId(id)
      .setReleaseLink(releaseLink)
      .setReleaseName(releaseName)
      .setReleaseType(of)
      .setSource(source)
      .setTimestamp(timestamp)
      .setUpdatedAt(updatedAt)
      .setVendor(vendor)
      .setVersionData(versionData);

    for (final var binary : release.binaries) {
      try {
        builder.addBinaries(toBinary(binary));
      } catch (final Exception e) {
        LOG.error("exception raised during binary parsing: ", e);
        this.errorReceiver.accept(
          AOV3Error.builder()
            .setContext("binary")
            .setException(e)
            .setMessage(e.getMessage())
            .setSource(this.source)
            .build());
      }
    }

    return builder.build();
  }

  @Override
  public AOV3AvailableReleases parseAvailableReleases()
    throws IOException
  {
    final AOV3AST.AOV3AvailableReleasesJSON ast =
      this.objectMapper.readerFor(AOV3AST.AOV3AvailableReleasesJSON.class)
        .readValue(this.stream);

    return AOV3AvailableReleases.builder()
      .addAllAvailableLTSReleases(ast.availableLTSReleases)
      .addAllAvailableReleases(ast.availableReleases)
      .setMostRecentFeatureRelease(ast.mostRecentFeatureRelease)
      .setMostRecentLTSRelease(ast.mostRecentLTS)
      .build();
  }

  @Override
  public List<String> parseReleaseNames()
    throws IOException
  {
    final AOV3AST.AOV3ReleaseNamesJSON ast =
      this.objectMapper.readerFor(AOV3AST.AOV3ReleaseNamesJSON.class)
        .readValue(this.stream);

    return List.copyOf(ast.releases);
  }

  @Override
  public List<AOV3VersionData> parseReleaseVersions()
    throws IOException
  {
    final AOV3AST.AOV3ReleaseVersionsJSON ast =
      this.objectMapper.readerFor(AOV3AST.AOV3ReleaseVersionsJSON.class)
        .readValue(this.stream);

    return ast.versions.stream()
      .flatMap(this::tryToVersionData)
      .collect(Collectors.toList());
  }

  private Stream<? extends AOV3VersionData> tryToVersionData(
    final AOV3AST.AOV3ReleaseVersionJSON version)
  {
    try {
      return Stream.of(toVersionData(version));
    } catch (final Exception e) {
      LOG.error("{}: error encountered during parsing version data: ",
                this.source, e);
      this.errorReceiver.accept(
        AOV3Error.builder()
          .setContext("version")
          .setException(e)
          .setMessage(e.getMessage())
          .setSource(this.source)
          .build());
      return Stream.empty();
    }
  }

  @Override
  public List<AOV3Release> parseAssetsForRelease()
    throws IOException
  {
    final List<AOV3AST.AOV3ReleaseJSON> ast =
      this.objectMapper.readerFor(new TypeReference<List<AOV3AST.AOV3ReleaseJSON>>()
      {
      }).readValue(this.stream);

    return ast.stream()
      .flatMap(this::tryToRelease)
      .collect(Collectors.toList());
  }

  private Stream<? extends AOV3Release> tryToRelease(
    final AOV3AST.AOV3ReleaseJSON release)
  {
    try {
      return Stream.of(this.toRelease(release));
    } catch (final Exception e) {
      LOG.error("exception raised during release parsing: ", e);
      this.errorReceiver.accept(
        AOV3Error.builder()
          .setContext("release")
          .setException(e)
          .setMessage(e.getMessage())
          .setSource(this.source)
          .build());
      return Stream.empty();
    }
  }
}
