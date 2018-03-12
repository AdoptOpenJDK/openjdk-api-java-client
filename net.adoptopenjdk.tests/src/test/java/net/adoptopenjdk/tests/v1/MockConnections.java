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
import net.adoptopenjdk.v1.AOv1HTTPConnectionsType;

import javax.net.ssl.HostnameVerifier;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

final class MockConnections implements AOv1HTTPConnectionsType
{
  private final Queue<MockConnection> queue;

  MockConnections()
  {
    this.queue = new ArrayDeque<>();
  }

  public Queue<MockConnection> queue()
  {
    return this.queue;
  }

  @Override
  public AOv1HTTPConnectionType head(
    final URI uri,
    final Map<String, String> headers)
  {
    return this.queue.remove();
  }

  @Override
  public AOv1HTTPConnectionType get(
    final URI uri,
    final Map<String, String> headers)
  {
    return this.queue.remove();
  }

  public void addConnection(final MockConnection connection)
  {
    this.queue.add(connection);
  }
}
