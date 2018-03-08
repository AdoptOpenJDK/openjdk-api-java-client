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

package net.adoptopenjdk.spi;

import org.immutables.value.Value;

/**
 * A build variant (such as {@code "openjdk8"}).
 */

@ImmutableStyleType
@Value.Immutable
public interface AOVariantType
{
  /**
   * @return A variant identifier such as {@code "openjdk8"}.
   */

  @Value.Parameter
  String name();

  /**
   * @return A humanly-readable description of the variant (such as {@code "OpenJDK 8 with Hotspot"})
   */

  @Value.Parameter
  String description();
}
