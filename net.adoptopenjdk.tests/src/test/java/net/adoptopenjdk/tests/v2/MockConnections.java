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

package net.adoptopenjdk.tests.v2;

import net.adoptopenjdk.v2.AOv2HTTPConnectionType;
import net.adoptopenjdk.v2.AOv2HTTPConnectionsType;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public final class MockConnections implements AOv2HTTPConnectionsType
{
  private final Queue<MockConnection> queue;

  public MockConnections()
  {
    this.queue = new ArrayDeque<>();
  }

  public Queue<MockConnection> queue()
  {
    return this.queue;
  }

  @Override
  public AOv2HTTPConnectionType head(
    final URI uri,
    final Map<String, String> headers)
  {
    return this.queue.remove();
  }

  @Override
  public AOv2HTTPConnectionType get(
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
