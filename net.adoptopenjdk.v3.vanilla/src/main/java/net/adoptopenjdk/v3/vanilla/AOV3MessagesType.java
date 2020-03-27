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

package net.adoptopenjdk.v3.vanilla;

import java.net.URI;
import java.util.ResourceBundle;

/**
 * A provider of translated strings.
 */

public interface AOV3MessagesType
{
  /**
   * An HTTP request failed...
   *
   * @param statusCode The HTTP status code
   * @param uri        The URI
   *
   * @return A translated string
   */

  String requestFailed(
    int statusCode,
    URI uri);

  /**
   * @return The underlying resource bundle
   */

  ResourceBundle resourceBundle();

  /**
   * Format a message.
   *
   * @param id   The message ID
   * @param args The arguments
   *
   * @return A formatted message
   */

  String format(
    String id,
    Object... args);
}
