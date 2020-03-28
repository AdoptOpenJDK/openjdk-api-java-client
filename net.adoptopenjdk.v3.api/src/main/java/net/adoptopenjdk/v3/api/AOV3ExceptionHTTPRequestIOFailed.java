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

import java.net.URI;
import java.util.Objects;

/**
 * An HTTP request due to an I/O error.
 */

public final class AOV3ExceptionHTTPRequestIOFailed extends AOV3Exception
{
  private final URI uri;

  /**
   * Construct an exception.
   *
   * @param inUri The request URI
   * @param cause The cause of the error
   */

  public AOV3ExceptionHTTPRequestIOFailed(
    final URI inUri,
    final Throwable cause)
  {
    super(cause);
    this.uri =
      Objects.requireNonNull(inUri, "uri");
  }

  /**
   * @return The request URI
   */

  public URI uri()
  {
    return this.uri;
  }
}
