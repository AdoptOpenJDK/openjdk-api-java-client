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

package net.adoptopenjdk.v3.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public final class AOV3Fuzzing
{
  private AOV3Fuzzing()
  {

  }

  public static InputStream filterRemoveNodes(
    final Random random,
    final InputStream stream,
    final double chance)
    throws IOException
  {
    final var objectMapper = new ObjectMapper();
    final var root = objectMapper.readTree(stream);
    filter(random, root, chance);
    final var output = new ByteArrayOutputStream();
    objectMapper.writeValue(output, root);
    return new ByteArrayInputStream(output.toByteArray());
  }

  private static void filter(
    final Random random,
    final JsonNode root,
    final double chance)
  {
    if (root.isArray()) {
      final var arrayNode = (ArrayNode) root;
      final var iter = arrayNode.elements();
      while (iter.hasNext()) {
        final var next = iter.next();
        if (random.nextDouble() < chance) {
          iter.remove();
        } else {
          filter(random, next, chance);
        }
      }
    } else if (root.isObject()) {
      final var objectNode = (ObjectNode) root;
      final var iter = objectNode.elements();
      while (iter.hasNext()) {
        final var next = iter.next();
        if (random.nextDouble() < chance) {
          iter.remove();
        } else {
          filter(random, next, chance);
        }
      }
    }
  }
}
