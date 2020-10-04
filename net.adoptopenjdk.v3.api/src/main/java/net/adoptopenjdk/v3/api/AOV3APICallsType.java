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
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info
   * _available_releases"
   */

  AOV3RequestReleasesType availableReleases(
    Consumer<AOV3Error> errorReceiver);

  /**
   * @param errorReceiver A receiver of errors encountered during the API call
   * @param page          The page number
   * @param pageSize      The page size
   * @param releaseKind   The target release kind
   * @param sortOrder     The result sort order
   * @param vendor        The target vendor
   * @param versionRange  The range of versions to fetch
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info
   * _release_names"
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
   * @param page          The page number
   * @param pageSize      The page size
   * @param releaseKind   The target release kind
   * @param sortOrder     The result sort order
   * @param vendor        The target vendor
   * @param versionRange  The range of versions to fetch
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Release%20Info/get_v3_info
   * _release_versions"
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
   * @param errorReceiver     A receiver of errors encountered during the API call
   * @param page              The page number
   * @param pageSize          The page size
   * @param version           The target version
   * @param releaseKind       The target release kind
   * @param architecture      The target architecture
   * @param heapSize          The target heap size
   * @param imageKind         The target image kind
   * @param jvmImplementation The target JVM implementation
   * @param operatingSystem   The target operating system
   * @param project           The target project
   * @param sortOrder         The result sort order
   * @param vendor            The target vendor
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Assets/get_v3_assets
   * _feature_releases__feature_version___release_type_"
   */

  // CHECKSTYLE:OFF
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
  // CHECKSTYLE:ON
  );


  /**
   * @param errorReceiver     A receiver of errors encountered during the API call
   * @param version           The target version
   * @param jvmImplementation The target JVM implementation
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Assets/get_v3_assets
   * _latest__feature_version___jvm_impl_"
   */

  AOV3RequestAssetsForLatestType assetsForLatest(
    Consumer<AOV3Error> errorReceiver,
    BigInteger version,
    AOV3JVMImplementation jvmImplementation
  );

  /**
   * @param errorReceiver     A receiver of errors encountered during the API call
   * @param version           The target version
   * @param releaseKind       The target release kind
   * @param architecture      The target architecture
   * @param heapSize          The target heap size
   * @param imageKind         The target image kind
   * @param jvmImplementation The target JVM implementation
   * @param operatingSystem   The target operating system
   * @param project           The target project
   * @param vendor            The target vendor
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Binary/get_v3_binary
   * _latest__feature_version___release_type___os___arch___image_type___jvm_impl
   * ___heap_size___vendor_"
   */

  AOV3RequestBinaryForLatestType binaryForLatest(
    Consumer<AOV3Error> errorReceiver,
    AOV3Architecture architecture,
    BigInteger version,
    AOV3HeapSize heapSize,
    AOV3ImageKind imageKind,
    AOV3JVMImplementation jvmImplementation,
    AOV3OperatingSystem operatingSystem,
    AOV3ReleaseKind releaseKind,
    AOV3Vendor vendor,
    Optional<String> project
  );

  /**
   * @param errorReceiver     A receiver of errors encountered during the API call
   * @param architecture      The target architecture
   * @param heapSize          The target heap size
   * @param imageKind         The target image kind
   * @param jvmImplementation The target JVM implementation
   * @param releaseName       The name of the release
   * @param operatingSystem   The target operating system
   * @param project           The target project
   * @param vendor            The target vendor
   *
   * @return An executable request
   *
   * @see "https://api.adoptopenjdk.net/swagger-ui/#/Binary/get_v3_binary
   * _version__release_name___os___arch___image_type___jvm_impl___heap_size
   * ___vendor_"
   */

  AOV3RequestBinaryForReleaseType binaryForRelease(
    Consumer<AOV3Error> errorReceiver,
    String releaseName,
    AOV3OperatingSystem operatingSystem,
    AOV3Architecture architecture,
    AOV3ImageKind imageKind,
    AOV3JVMImplementation jvmImplementation,
    AOV3HeapSize heapSize,
    AOV3Vendor vendor,
    Optional<String> project
  );
}
