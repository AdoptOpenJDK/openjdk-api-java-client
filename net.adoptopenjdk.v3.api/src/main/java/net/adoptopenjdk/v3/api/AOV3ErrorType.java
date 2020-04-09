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

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.net.URI;
import java.util.Optional;

/**
 * An error that occurred during an API call.
 */

@Value.Immutable
@ImmutablesStyleType
public interface AOV3ErrorType
{
  /**
   * @return The source of the error
   */

  URI source();

  /**
   * @return The context of the error
   */

  String context();

  /**
   * @return The error message
   */

  String message();

  /**
   * @return The exception raised, if any
   */

  Optional<Exception> exception();

  /**
   * @return The error as a humanly readable string
   */

  default String show()
  {
    return String.format(
      "%s: %s: %s",
      this.source(),
      this.context(),
      this.message()
    );
  }
}
