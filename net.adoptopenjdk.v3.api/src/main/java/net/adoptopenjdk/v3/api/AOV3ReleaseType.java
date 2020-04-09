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

package net.adoptopenjdk.v3.api;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.math.BigInteger;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Information about a release.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3ReleaseType
{
  /**
   * @return The release binaries
   */

  List<AOV3BinaryType> binaries();

  /**
   * @return The number of times the release has been downloaded
   */

  BigInteger downloadCount();

  /**
   * @return The release identifier
   */

  String id();

  /**
   * @return The release link
   */

  URI releaseLink();

  /**
   * @return The release name
   */

  String releaseName();

  /**
   * @return The release availability type
   */

  AOV3ReleaseKind releaseType();

  /**
   * @return The source information, if any
   */

  Optional<AOV3Source> source();

  /**
   * @return The release timestamp
   */

  OffsetDateTime timestamp();

  /**
   * @return The release last modification time
   */

  OffsetDateTime updatedAt();

  /**
   * @return The release vendor
   */

  AOV3Vendor vendor();

  /**
   * @return The release version information
   */

  AOV3VersionData versionData();
}
