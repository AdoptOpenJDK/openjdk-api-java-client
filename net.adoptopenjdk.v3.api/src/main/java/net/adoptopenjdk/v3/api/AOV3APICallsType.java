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

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The set of API calls available in the AdoptOpenJDK v3 API.
 */

public interface AOV3APICallsType
{
  /**
   * @param errorReceiver A receiver of errors encountered during the API call
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info_available_releases"
   */

  AOV3RequestReleasesType availableReleases(
    Consumer<AOV3Error> errorReceiver);

  /**
   * @param errorReceiver A receiver of errors encountered during the API call
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info_release_names"
   */

  AOV3RequestReleaseNamesType releaseNames(
    Consumer<AOV3Error> errorReceiver,
    BigInteger page,
    BigInteger pageSize,
    Optional<AOV3ReleaseKind> releaseKind,
    Optional<AOV3SortOrder> sortOrder,
    Optional<AOV3Vendor> vendor,
    Optional<AOV3VersionRange> versionRange
  );

  /**
   * @param errorReceiver A receiver of errors encountered during the API call
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info_release_versions"
   */

  AOV3RequestReleaseVersionsType releaseVersions(
    Consumer<AOV3Error> errorReceiver,
    BigInteger page,
    BigInteger pageSize,
    Optional<AOV3ReleaseKind> releaseKind,
    Optional<AOV3SortOrder> sortOrder,
    Optional<AOV3Vendor> vendor,
    Optional<AOV3VersionRange> versionRange
  );

  /**
   * @param errorReceiver A receiver of errors encountered during the API call
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Assets/get_v3_assets_feature_releases__feature_version___release_type_"
   */

  AOV3RequestAssetsForReleaseType assetsForRelease(
    Consumer<AOV3Error> errorReceiver,
    BigInteger page,
    BigInteger pageSize,
    BigInteger version,
    AOV3ReleaseKind releaseKind,
    Optional<AOV3Architecture> architecture,
    Optional<AOV3HeapSize> heapSize,
    Optional<AOV3ImageKind> imageKind,
    Optional<AOV3JVMImplementation> jvmImplementation,
    Optional<AOV3OperatingSystem> operatingSystem,
    Optional<String> project,
    Optional<AOV3SortOrder> sortOrder,
    Optional<AOV3Vendor> vendor
  );
}
