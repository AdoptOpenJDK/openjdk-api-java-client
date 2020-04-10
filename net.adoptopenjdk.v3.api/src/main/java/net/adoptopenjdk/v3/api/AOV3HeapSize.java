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
 * A JVM heap size configuration.
 */

public enum AOV3HeapSize implements AOV3HasNameTextType
{
  /**
   * Large heap size limits
   */

  LARGE("large"),

  /**
   * Normal heap size limits
   */

  NORMAL("normal");

  private final String nameText;

  AOV3HeapSize(final String name)
  {
    this.nameText = Objects.requireNonNull(name, "name");
  }

  /**
   * Parse a value from the given string (the inverse of {@link #nameText()}.
   *
   * @param heapSize The heap size name
   *
   * @return A heap size
   */

  public static AOV3HeapSize of(
    final String heapSize)
  {
    return valueOf(heapSize.toUpperCase());
  }

  @Override
  public String nameText()
  {
    return this.nameText;
  }
}
