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

import java.util.Objects;

/**
 * The type of a release.
 */

public enum AOV3ReleaseKind implements AOV3HasNameTextType
{
  /**
   * Early-access code.
   */

  EARLY_ACCESS("ea"),

  /**
   * General availability code.
   */

  GENERAL_AVAILABILITY("ga");

  private final String nameText;

  AOV3ReleaseKind(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param kind The release type name
   *
   * @return A release type
   */

  public static AOV3ReleaseKind of(
    final String kind)
  {
    switch (kind) {
      case "ea":
        return EARLY_ACCESS;
      case "ga":
        return GENERAL_AVAILABILITY;
      default:
        throw new IllegalStateException("Unexpected value: " + kind);
    }
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
