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
 * An operating system.
 */

public enum AOV3OperatingSystem implements AOV3HasNameTextType
{
  AIX("aix"),
  LINUX("linux"),
  MAC("mac"),
  SOLARIS("solaris"),
  WINDOWS("windows");

  private final String nameText;

  AOV3OperatingSystem(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param operatingSystem The operating system name
   *
   * @return An operating system
   */

  public static AOV3OperatingSystem of(
    final String operatingSystem)
  {
    return valueOf(operatingSystem.toUpperCase());
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
