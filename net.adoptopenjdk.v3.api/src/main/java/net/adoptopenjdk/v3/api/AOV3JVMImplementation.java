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
 * A JVM implementation such as Hotspot, OpenJ9, etc.
 */

public enum AOV3JVMImplementation implements AOV3HasNameTextType
{
  /**
   * Hotspot
   */

  HOTSPOT("hotspot"),

  /**
   * IBM OpenJ9
   */

  OPENJ9("openj9");

  private final String nameText;

  AOV3JVMImplementation(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param jvmImplementation The jvm implementation name
   *
   * @return A jvm implementation
   */

  public static AOV3JVMImplementation of(
    final String jvmImplementation)
  {
    return valueOf(jvmImplementation.toUpperCase());
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
