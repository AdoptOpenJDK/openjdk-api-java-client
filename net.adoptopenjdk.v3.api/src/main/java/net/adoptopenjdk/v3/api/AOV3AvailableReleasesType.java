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
import java.util.List;

/**
 * A set of available releases.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3AvailableReleasesType
{
  /**
   * @return The version numbers of the available LTS releases
   */

  List<BigInteger> availableLTSReleases();

  /**
   * @return The version numbers of the available releases
   */

  List<BigInteger> availableReleases();

  /**
   * @return The version number of the most recent feature release
   */

  BigInteger mostRecentFeatureRelease();

  /**
   * @return The version number of the most recent LTS release
   */

  BigInteger mostRecentLTSRelease();
}
