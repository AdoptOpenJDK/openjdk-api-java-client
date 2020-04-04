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

package net.adoptopenjdk.v3.vanilla.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class AOV3ObjectMappers
{
  private AOV3ObjectMappers()
  {

  }

  public static ObjectMapper createObjectMapper()
  {
    final JsonMapper mapper =
      JsonMapper.builder()
        .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
        .build();

    final var deserializers = AOV3Deserializers.create();
    final var simpleModule = new SimpleModule();
    simpleModule.setDeserializers(deserializers);
    mapper.registerModule(simpleModule);
    return mapper;
  }
}
