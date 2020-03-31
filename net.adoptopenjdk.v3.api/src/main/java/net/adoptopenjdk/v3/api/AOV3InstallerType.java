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

package net.adoptopenjdk.v3.api;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.math.BigInteger;
import java.net.URI;
import java.util.Optional;

/**
 * Installer information.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3InstallerType
{
  /**
   * @return The checksum of the data
   */

  String checksum();

  /**
   * @return A link to the checksum of the data
   */

  Optional<URI> checksumLink();

  /**
   * @return The number of times the package has been downloaded
   */

  BigInteger downloadCount();

  /**
   * @return The link to the data
   */

  URI link();

  /**
   * @return The package name
   */

  String name();

  /**
   * @return The link to the package signature
   */

  Optional<URI> signatureLink();

  /**
   * @return The package size
   */

  BigInteger size();
}
