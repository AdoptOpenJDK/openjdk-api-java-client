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
 * The available JVM architectures.
 */

public enum AOV3Architecture implements AOV3HasNameTextType
{
  /**
   * AARCH64
   */

  AARCH64("aarch64"),

  /**
   * ARM
   */

  ARM("arm"),

  /**
   * PPC64
   */

  PPC64("ppc64"),

  /**
   * PPC64LE
   */

  PPC64LE("ppc64le"),

  /**
   * S390X
   */

  S390X("s390x"),

  /**
   * SPARCV9
   */

  SPARCV9("sparcv9"),

  /**
   * X32
   */

  X32("x32"),

  /**
   * X64
   */

  X64("x64");

  private final String nameText;

  AOV3Architecture(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param architecture The architecture name
   *
   * @return An architecture
   */

  public static AOV3Architecture of(
    final String architecture)
  {
    return valueOf(architecture.toUpperCase());
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
