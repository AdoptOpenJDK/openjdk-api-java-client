/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com>
 *
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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.adoptopenjdk.v3.api.AOV3Error;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;

public final class AOV3ResponseParsers implements AOV3ResponseParsersType
{
  private final ObjectMapper objectMapper;

  private AOV3ResponseParsers(
    final ObjectMapper inObjectMapper)
  {
    this.objectMapper =
      Objects.requireNonNull(inObjectMapper, "objectMapper");
  }

  public static AOV3ResponseParsersType create()
  {
    return new AOV3ResponseParsers(
      AOV3ObjectMappers.createObjectMapper());
  }

  @Override
  public AOV3ResponseParserType createParser(
    final Consumer<AOV3Error> errorReceiver,
    final URI source,
    final InputStream stream)
  {
    return new AOV3ResponseParser(
      errorReceiver,
      this.objectMapper,
      source,
      stream
    );
  }
}
