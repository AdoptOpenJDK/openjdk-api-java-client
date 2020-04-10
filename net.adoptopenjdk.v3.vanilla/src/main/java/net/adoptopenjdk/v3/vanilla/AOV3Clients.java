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

package net.adoptopenjdk.v3.vanilla;

import net.adoptopenjdk.v3.api.AOV3ClientProviderType;
import net.adoptopenjdk.v3.api.AOV3ClientType;
import net.adoptopenjdk.v3.vanilla.internal.AOV3Client;
import net.adoptopenjdk.v3.vanilla.internal.AOV3Messages;
import net.adoptopenjdk.v3.vanilla.internal.AOV3MessagesType;
import net.adoptopenjdk.v3.vanilla.internal.AOV3ResponseParsers;
import net.adoptopenjdk.v3.vanilla.internal.AOV3ResponseParsersType;
import org.osgi.service.component.annotations.Component;

import java.net.http.HttpClient;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The default provider of v3 clients.
 */

@Component(service = AOV3ClientProviderType.class)
public final class AOV3Clients implements AOV3ClientProviderType
{
  private final Supplier<HttpClient> clients;
  private final AOV3ResponseParsersType parsers;
  private final AOV3MessagesType messages;

  /**
   * Construct a client provider.
   *
   * @param inParsers  The parser provider
   * @param inMessages The message provider
   * @param inClients  The HTTP client supplier
   */

  public AOV3Clients(
    final AOV3ResponseParsersType inParsers,
    final AOV3MessagesType inMessages,
    final Supplier<HttpClient> inClients)
  {
    this.parsers =
      Objects.requireNonNull(inParsers, "parsers");
    this.messages =
      Objects.requireNonNull(inMessages, "messages");
    this.clients =
      Objects.requireNonNull(inClients, "client");
  }

  /**
   * Construct a client provider.
   */

  public AOV3Clients()
  {
    this(
      AOV3ResponseParsers.create(),
      AOV3Messages.of(AOV3Messages.getResourceBundle()),
      AOV3Clients::newHttpClient
    );
  }

  private static HttpClient newHttpClient()
  {
    return HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NEVER)
      .build();
  }

  @Override
  public AOV3ClientType createClient()
  {
    return new AOV3Client(
      "https://api.adoptopenjdk.net/v3",
      this.clients.get(),
      this.messages,
      this.parsers
    );
  }
}
