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

import net.adoptopenjdk.v3.api.AOV3ClientProviderType;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3ExceptionHTTPRequestFailed;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.vanilla.AOV3Clients;
import net.adoptopenjdk.v3.vanilla.AOV3Messages;
import net.adoptopenjdk.v3.vanilla.AOV3MessagesType;
import net.adoptopenjdk.v3.vanilla.AOV3ResponseParserType;
import net.adoptopenjdk.v3.vanilla.AOV3ResponseParsersType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public final class AOV3ClientsTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV3ClientsTest.class);

  private HttpClient client;
  private AOV3ResponseParsersType parsers;
  private AOV3MessagesType messages;
  private ArrayList<AOV3Error> errors;
  private HttpResponse<Object> response;
  private AOV3ResponseParserType parser;

  @BeforeEach
  public void testSetup()
  {
    this.client =
      Mockito.mock(HttpClient.class);
    this.parsers =
      Mockito.mock(AOV3ResponseParsersType.class);
    this.parser =
      Mockito.mock(AOV3ResponseParserType.class);
    this.messages =
      AOV3Messages.of(AOV3Messages.getResourceBundle());
    this.errors =
      new ArrayList<AOV3Error>();
    this.response =
      Mockito.mock(HttpResponse.class);
  }

  private void logError(
    final AOV3Error error)
  {
    LOG.error("error: {}", error);
    this.errors.add(error);
  }

  @Test
  public void testRequestFailure0()
    throws Exception
  {
    final var clients =
      new AOV3Clients(this.parsers, this.messages, () -> this.client);

    final var headers =
      HttpHeaders.of(Map.of(), (key, val) -> true);

    Mockito.when(this.client.send(Mockito.any(), Mockito.any()))
      .thenReturn(this.response);
    Mockito.when(Integer.valueOf(this.response.statusCode()))
      .thenReturn(Integer.valueOf(500));
    Mockito.when(this.response.headers())
      .thenReturn(headers);
    Mockito.when(this.response.uri())
      .thenReturn(URI.create("urn:test"));

    try (var client = clients.createClient()) {
      final var exception =
        Assertions.assertThrows(AOV3ExceptionHTTPRequestFailed.class, () -> {
          client.assetsForRelease(
            this::logError,
            BigInteger.ZERO,
            BigInteger.TEN,
            BigInteger.valueOf(11L),
            AOV3ReleaseKind.GENERAL_AVAILABILITY,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
          ).execute();
        });

      Assertions.assertEquals(
        500,
        exception.statusCode()
      );
      Assertions.assertEquals(
        "urn:test",
        exception.uri().toString()
      );
    }
  }

  @Test
  public void testService()
    throws Exception
  {
    final var clients =
      ServiceLoader.load(AOV3ClientProviderType.class)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
          String.format("No implementations of %s are available", AOV3ClientProviderType.class)));
  }
}
