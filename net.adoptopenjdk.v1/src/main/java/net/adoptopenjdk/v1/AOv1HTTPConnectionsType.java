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
import java.util.Map;

/**
 * A provider of HTTP connections.
 */

public interface AOv1HTTPConnectionsType
{
  /**
   * Send a HEAD request to the given URI.
   *
   * @param uri     The URI
   * @param headers The client headers
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  AOv1HTTPConnectionType head(
    URI uri,
    Map<String, String> headers)
    throws AOException, AOv1HTTPException;

  /**
   * Send a HEAD request to the given URI.
   *
   * @param uri The URI
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  default AOv1HTTPConnectionType head(
    final URI uri)
    throws AOException, AOv1HTTPException
  {
    return this.head(uri, Map.of());
  }

  /**
   * Send a GET request to the given URI.
   *
   * @param uri     The URI
   * @param headers The client headers
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  AOv1HTTPConnectionType get(
    URI uri,
    Map<String, String> headers)
    throws AOException, AOv1HTTPException;

  /**
   * Send a GET request to the given URI.
   *
   * @param uri The URI
   *
   * @return An open connection
   *
   * @throws AOv1HTTPException If the server returns an error code {@code >= 400}
   * @throws AOException       On other errors
   */

  default AOv1HTTPConnectionType get(
    final URI uri)
    throws AOException, AOv1HTTPException
  {
    return this.get(uri, Map.of());
  }
}
