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

import java.util.Objects;

/**
 * An exception raised when parsing data.
 */

public final class AOParseException extends AOException
{
  /**
   * Construct an exception.
   *
   * @param message The exception message
   */

  public AOParseException(final String message)
  {
    super(Objects.requireNonNull(message, "message"));
  }

  /**
   * Construct an exception.
   *
   * @param cause   The cause
   * @param message The exception message
   */

  public AOParseException(
    final String message,
    final Throwable cause)
  {
    super(
      Objects.requireNonNull(message, "message"),
      Objects.requireNonNull(cause, "cause"));
  }
}
