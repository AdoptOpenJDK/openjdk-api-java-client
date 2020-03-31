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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An HTTP request failed because the server returned some sort of error.
 */

public final class AOV3ExceptionHTTPRequestFailed extends AOV3Exception
{
  private final int statusCode;
  private final Map<String, List<String>> headers;
  private final URI uri;

  /**
   * Construct an exception.
   *
   * @param inStatusCode The HTTP status code
   * @param inUri        The URI of the request
   * @param inMessage    The error message
   * @param inHeaders    The response headers
   */

  public AOV3ExceptionHTTPRequestFailed(
    final int inStatusCode,
    final URI inUri,
    final String inMessage,
    final Map<String, List<String>> inHeaders)
  {
    super(inMessage);
    this.statusCode =
      inStatusCode;
    this.uri =
      Objects.requireNonNull(inUri, "uri");
    this.headers =
      Objects.requireNonNull(inHeaders, "headers");
  }

  /**
   * @return The URI of the request
   */

  public URI uri()
  {
    return this.uri;
  }

  /**
   * @return The HTTP status code
   */

  public int statusCode()
  {
    return this.statusCode;
  }

  /**
   * @return The response headers
   */

  public Map<String, List<String>> headers()
  {
    return this.headers;
  }
}
