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

/**
 * An executable request. Requests can be created once and re-executed an
 * arbitrary number of times.
 *
 * @param <T> The type of returned values
 */

public interface AOV3RequestType<T>
{
  /**
   * Execute the request, returning the results received.
   *
   * @return The received data
   *
   * @throws AOV3Exception        On errors
   * @throws InterruptedException If the thread hosting the operation was interrupted
   */

  T execute()
    throws AOV3Exception, InterruptedException;
}
