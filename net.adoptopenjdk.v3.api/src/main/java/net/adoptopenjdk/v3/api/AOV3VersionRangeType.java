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

/**
 * A version range consisting of two bounds.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3VersionRangeType
{
  /**
   * @return The lower bound
   */

  @Value.Parameter
  AOV3VersionBound lower();

  /**
   * @return The upper bound
   */

  @Value.Parameter
  AOV3VersionBound upper();

  /**
   * @return The range as a string, such as {@code [11,12)}
   */

  default String toText()
  {
    final var lower = this.lower();
    final var upper = this.upper();
    return String.format(
      "%s%s,%s%s",
      lower.isExclusive() ? "(" : "[",
      lower.name().orElse(""),
      upper.name().orElse(""),
      upper.isExclusive() ? ")" : "]"
    );
  }
}
