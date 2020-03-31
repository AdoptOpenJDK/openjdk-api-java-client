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

import java.util.Objects;

/**
 * A JDK build vendor.
 */

public enum AOV3Vendor implements AOV3HasNameTextType
{
  ADOPT_OPENJDK("adoptopenjdk"),
  OPENJDK("openjdk");

  private final String nameText;

  AOV3Vendor(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param vendor The vendor name
   *
   * @return A vendor
   */

  public static AOV3Vendor of(
    final String vendor)
  {
    switch (vendor) {
      case "adoptopenjdk":
        return ADOPT_OPENJDK;
      case "openjdk":
        return OPENJDK;
      default:
        throw new IllegalStateException("Unexpected value: " + vendor);
    }
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
