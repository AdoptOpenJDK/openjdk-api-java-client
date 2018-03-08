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

package net.adoptopenjdk.tests.v1;

import net.adoptopenjdk.v1.AOv1HTTPConnectionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class MockConnection implements AOv1HTTPConnectionType
{
  private final URI uri;
  private final Map<String, List<String>> headers;
  private final InputStream stream;

  MockConnection(
    final URI in_uri,
    final Map<String, List<String>> in_headers,
    final InputStream in_stream)
  {
    this.uri =
      Objects.requireNonNull(in_uri, "uri");
    this.headers =
      Objects.requireNonNull(in_headers, "headers");
    this.stream =
      Objects.requireNonNull(in_stream, "stream");

  }

  static MockConnection create(
    final Map<String, String> in_headers)
  {
    return new MockConnection(
      URI.create("anything"),
      expandHeaders(in_headers),
      new ByteArrayInputStream(new byte[0]));
  }

  static MockConnection createWithData(
    final Map<String, String> in_headers,
    final InputStream stream)
  {
    return new MockConnection(
      URI.create("anything"),
      expandHeaders(in_headers),
      stream);
  }

  private static Map<String, List<String>> expandHeaders(
    final Map<String, String> in_headers)
  {
    return Objects.requireNonNull(in_headers, "headers")
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> List.of(e.getValue())
      ));
  }

  @Override
  public InputStream input()
  {
    return this.stream;
  }

  @Override
  public Map<String, List<String>> headers()
  {
    return this.headers;
  }

  @Override
  public void close()
  {

  }
}
