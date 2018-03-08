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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * An HTTP connection.
 *
 * This is a very simple abstraction over HTTP connections to allow for easier
 * unit testing (and for switching HTTP client implementations at a later date).
 */

public interface AOv1HTTPConnectionType extends Closeable
{
  /**
   * @return The input stream
   *
   * @throws IOException On I/O errors (typically if the connection is already closed)
   */

  InputStream input()
    throws IOException;

  /**
   * @return The response headers
   */

  Map<String, List<String>> headers();
}
