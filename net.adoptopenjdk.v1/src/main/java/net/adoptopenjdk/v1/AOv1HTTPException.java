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

package net.adoptopenjdk.v1;

import net.adoptopenjdk.spi.AOException;

import java.net.URI;
import java.util.Objects;

/**
 * An exception caused by a failed HTTP request.
 */

public final class AOv1HTTPException extends AOException
{
  private final int code;
  private final String server_message;
  private final URI uri;
  private final AOv1HTTPProblemReport report;

  /**
   * Construct an exception.
   *
   * @param in_code           The HTTP error code
   * @param in_server_message The error message received from the server
   * @param in_uri            The URI
   * @param in_report         The error report
   */

  public AOv1HTTPException(
    final int in_code,
    final String in_server_message,
    final URI in_uri,
    final AOv1HTTPProblemReport in_report)
  {
    super(
      String.format(
        "%s: %d - %s",
        Objects.requireNonNull(in_uri, "uri"),
        Integer.valueOf(in_code),
        Objects.requireNonNull(in_server_message, "server_message")));

    this.code = in_code;
    this.server_message = in_server_message;
    this.uri = in_uri;
    this.report = Objects.requireNonNull(in_report, "report");
  }

  /**
   * @return The HTTP status code
   */

  public int statusCode()
  {
    return this.code;
  }

  /**
   * @return The HTTP server error message
   */

  public String serverMessage()
  {
    return this.server_message;
  }

  /**
   * @return The original URI
   */

  public URI uri()
  {
    return this.uri;
  }

  /**
   * @return The problem report returned by the server in the case of errors
   */

  public AOv1HTTPProblemReport report()
  {
    return this.report;
  }
}
