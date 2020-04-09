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
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * A description of a binary distribution.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3BinaryType
{
  /**
   * @return The target architecture
   */

  AOV3Architecture architecture();

  /**
   * @return The number of times the distribution has been downloaded
   */

  BigInteger downloadCount();

  /**
   * @return The heap size configuration
   */

  AOV3HeapSize heapSize();

  /**
   * @return The type of runtime image
   */

  AOV3ImageKind imageType();

  /**
   * @return The installer information
   */

  Optional<AOV3Installer> installer();

  /**
   * @return The underlying JVM implementation
   */

  AOV3JVMImplementation jvmImplementation();

  /**
   * @return The target operating system
   */

  AOV3OperatingSystem operatingSystem();

  /**
   * @return The package information
   */

  AOV3PackageType package_();

  /**
   * @return The project associated with the binary (such as "jdk")
   */

  String project();

  /**
   * @return The SCM commit for the build
   */

  Optional<String> scmReference();

  /**
   * @return The time the distribution was last updated
   */

  OffsetDateTime updatedAt();
}
