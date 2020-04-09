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
 * The base type of exceptions raised by the API.
 */

public class AOV3Exception extends Exception
{
  /**
   * Construct an exception.
   *
   * @param message The message
   */

  public AOV3Exception(
    final String message)
  {
    super(Objects.requireNonNull(message, "message"));
  }

  /**
   * Construct an exception.
   *
   * @param cause   The cause
   * @param message The message
   */

  public AOV3Exception(
    final String message,
    final Throwable cause)
  {
    super(
      Objects.requireNonNull(message, "message"),
      Objects.requireNonNull(cause, "cause")
    );
  }

  /**
   * Construct an exception.
   *
   * @param cause The cause
   */

  public AOV3Exception(
    final Throwable cause)
  {
    super(Objects.requireNonNull(cause, "cause"));
  }
}
