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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

/**
 * Jackson JSON AST types.
 */

// CHECKSTYLE:OFF
public final class AOV3AST
{
  /**
   * A marker interface for the AST types.
   */

  public interface AOV3ASTMemberType
  {

  }

  @JsonDeserialize
  public static final class AOV3ReleaseNamesJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "releases", required = true)
    List<String> releases = List.of();
  }

  @JsonDeserialize
  public static final class AOV3AvailableReleasesJSON
    implements AOV3ASTMemberType
  {
    @JsonProperty(value = "available_lts_releases", required = true)
    List<BigInteger> availableLTSReleases = List.of();

    @JsonProperty(value = "available_releases", required = true)
    List<BigInteger> availableReleases = List.of();

    @JsonProperty(value = "most_recent_feature_release", required = true)
    BigInteger mostRecentFeatureRelease = BigInteger.ZERO;

    @JsonProperty(value = "most_recent_lts", required = true)
    BigInteger mostRecentLTS = BigInteger.ZERO;
  }

  @JsonDeserialize
  public static final class AOV3ReleaseVersionJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "adopt_build_number")
    BigInteger adoptBuildNumber = BigInteger.ZERO;

    @JsonProperty(value = "build", required = true)
    BigInteger build = BigInteger.ZERO;

    @JsonProperty(value = "major", required = true)
    BigInteger major = BigInteger.ZERO;

    @JsonProperty(value = "minor", required = true)
    BigInteger minor = BigInteger.ZERO;

    @JsonProperty(value = "openjdk_version", required = true)
    String openjdkVersion = "";

    @JsonProperty(value = "optional")
    String optional = "";

    @JsonProperty(value = "pre")
    String pre = "";

    @JsonProperty(value = "security", required = true)
    BigInteger security = BigInteger.ZERO;

    @JsonProperty(value = "semver", required = true)
    String semver = "";

    @JsonProperty(value = "vendor")
    String vendor = "";
  }

  @JsonDeserialize
  public static final class AOV3ListBinaryAssetViewJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "binary", required = true)
    AOV3BinaryJSON binary;

    @JsonProperty(value = "release_name", required = true)
    String releaseName;

    @JsonProperty(value = "version", required = false)
    AOV3ReleaseVersionJSON versionData;
  }

  @JsonDeserialize
  public static final class AOV3ReleaseVersionsJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "versions", required = true)
    List<AOV3ReleaseVersionJSON> versions = List.of();
  }

  @JsonDeserialize
  public static final class AOV3InstallerJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "checksum", required = true)
    String checksum;

    @JsonProperty(value = "checksum_link", required = true)
    URI checksumLink;

    @JsonProperty(value = "download_count", required = true)
    BigInteger downloadCount = BigInteger.ZERO;

    @JsonProperty(value = "link", required = true)
    URI link;

    @JsonProperty(value = "name", required = true)
    String name = "";

    @JsonProperty(value = "signature_link")
    URI signatureLink;

    @JsonProperty(value = "size", required = true)
    BigInteger size = BigInteger.ZERO;
  }

  @JsonDeserialize
  public static final class AOV3PackageJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "checksum", required = true)
    String checksum;

    @JsonProperty(value = "checksum_link", required = true)
    URI checksumLink;

    @JsonProperty(value = "download_count", required = true)
    BigInteger downloadCount = BigInteger.ZERO;

    @JsonProperty(value = "link", required = true)
    URI link;

    @JsonProperty(value = "name", required = true)
    String name = "";

    @JsonProperty(value = "signature_link", required = true)
    URI signatureLink;

    @JsonProperty(value = "size", required = true)
    BigInteger size = BigInteger.ZERO;
  }

  @JsonDeserialize
  public static final class AOV3BinaryJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "architecture", required = true)
    String architecture;

    @JsonProperty(value = "download_count", required = true)
    BigInteger downloadCount = BigInteger.ZERO;

    @JsonProperty(value = "heap_size", required = true)
    String heapSize;

    @JsonProperty(value = "image_type", required = true)
    String imageType;

    @JsonProperty(value = "installer", required = true)
    AOV3InstallerJSON installer;

    @JsonProperty(value = "jvm_impl", required = true)
    String jvmImplementation;

    @JsonProperty(value = "os", required = true)
    String operatingSystem;

    @JsonProperty(value = "package", required = true)
    AOV3PackageJSON package_;

    @JsonProperty(value = "project", required = true)
    String project;

    @JsonProperty(value = "scm_ref", required = true)
    String scmReference;

    @JsonProperty(value = "updated_at", required = true)
    String updatedAt;
  }

  @JsonDeserialize
  public static final class AOV3SourceJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "link", required = true)
    URI link;

    @JsonProperty(value = "name", required = true)
    String name;

    @JsonProperty(value = "size", required = true)
    BigInteger size = BigInteger.ZERO;
  }

  @JsonDeserialize
  public static final class AOV3ReleaseJSON implements AOV3ASTMemberType
  {
    @JsonProperty(value = "binaries", required = true)
    List<AOV3BinaryJSON> binaries = List.of();

    @JsonProperty(value = "download_count", required = true)
    BigInteger downloadCount = BigInteger.ZERO;

    @JsonProperty(value = "id", required = true)
    String id;

    @JsonProperty(value = "release_link", required = true)
    URI releaseLink;

    @JsonProperty(value = "release_name", required = true)
    String releaseName;

    @JsonProperty(value = "release_type", required = true)
    String releaseType;

    @JsonProperty(value = "source")
    AOV3SourceJSON source;

    @JsonProperty(value = "timestamp", required = true)
    String timestamp;

    @JsonProperty(value = "updated_at", required = true)
    String updatedAt;

    @JsonProperty(value = "vendor", required = true)
    String vendor;

    @JsonProperty(value = "version_data", required = true)
    AOV3ReleaseVersionJSON versionData;
  }
}
