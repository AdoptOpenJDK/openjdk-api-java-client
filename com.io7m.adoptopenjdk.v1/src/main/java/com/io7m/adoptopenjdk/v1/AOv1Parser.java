/*
 * Copyright © 2018 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.adoptopenjdk.v1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.io7m.adoptopenjdk.spi.AOBinary;
import com.io7m.adoptopenjdk.spi.AOParseException;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.jaffirm.core.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

/**
 * A parser that can consume API 1.0 responses.
 */

public final class AOv1Parser
{
  private static final Logger LOG = LoggerFactory.getLogger(AOv1Parser.class);
  private static final Pattern WHITESPACE = Pattern.compile("\\s+");
  private static final Pattern SIZE_MEGABYTES = Pattern.compile("([0-9]+) MB");

  private final ResourceBundle messages;

  /**
   * Construct a new parser.
   */

  public AOv1Parser()
  {
    this.messages = ResourceBundle.getBundle("com/io7m/adoptopenjdk/v1/messages");
  }

  private static String oneOf(final JsonToken[] expected)
  {
    return List.of(expected)
      .stream()
      .map(JsonToken::toString)
      .collect(Collectors.joining(", "));
  }

  private static String formatLocation(
    final URI uri,
    final JsonLocation location)
  {
    return new StringBuilder(128)
      .append(uri)
      .append(" @ ")
      .append(location.getLineNr())
      .append(":")
      .append(location.getColumnNr())
      .toString();
  }

  /**
   * Parse an array of releases.
   *
   * @param uri    The URI of the stream for diagnostic messages
   * @param stream The input stream
   *
   * @return A list of releases
   *
   * @throws IOException      On I/O errors
   * @throws AOParseException On parse errors
   */

  public List<AORelease> parseReleases(
    final URI uri,
    final InputStream stream)
    throws AOParseException, IOException
  {
    Objects.requireNonNull(uri, "uri");
    Objects.requireNonNull(stream, "stream");

    final Instant time_then = Instant.now();

    try {
      final List<AORelease> releases = new ArrayList<>(8);
      final JsonFactory parsers = new JsonFactory();
      try (JsonParser parser = parsers.createParser(stream)) {
        while (true) {
          final JsonToken next = parser.nextToken();
          LOG.trace("{}: token: {}", uri, next);
          if (next == null) {
            break;
          }

          this.requireOneOf(uri, parser, next, START_ARRAY);
          this.parseReleasesArray(uri, parser, releases);
        }
      }

      return releases;
    } catch (final JsonProcessingException e) {
      throw new AOParseException(e.getMessage(), e);
    } finally {
      final Instant time_now = Instant.now();
      LOG.debug("parsed in {}", Duration.between(time_then, time_now));
    }
  }

  /**
   * Require that the received token be any of the given alternatives.
   */

  private void requireOneOf(
    final URI uri,
    final JsonParser parser,
    final JsonToken received,
    final JsonToken... expected)
    throws AOParseException
  {
    for (final JsonToken expect : expected) {
      if (expect == received) {
        return;
      }
    }

    throw this.parseErrorExpectedReceived(
      uri,
      parser,
      MessageFormat.format(
        this.messages.getString("json_one_of"),
        oneOf(expected)),
      received.toString());
  }

  /**
   * Require that the received token be anything other than EOF.
   */

  private void requireNotEOF(
    final URI uri,
    final JsonParser parser,
    final JsonToken received)
    throws AOParseException
  {
    if (received != null) {
      return;
    }

    throw this.parseErrorExpectedReceived(
      uri,
      parser,
      this.messages.getString("json_eof"),
      this.messages.getString("json_not_eof"));
  }

  /**
   * Parse an array of releases from the given parser.
   */

  private void parseReleasesArray(
    final URI uri,
    final JsonParser parser,
    final Collection<AORelease> releases)
    throws IOException, AOParseException
  {
    Preconditions.checkPrecondition(
      parser.currentToken() == START_ARRAY,
      "Parser must be at array start");

    while (true) {
      final JsonToken next = parser.nextToken();
      LOG.trace("{}: token: {}", uri, next);
      this.requireNotEOF(uri, parser, next);

      this.requireOneOf(uri, parser, next, START_OBJECT, END_ARRAY);
      if (next == END_ARRAY) {
        return;
      }

      releases.add(this.parseRelease(uri, parser));
    }
  }

  /**
   * Parse a single release from the given parser.
   */

  private AORelease parseRelease(
    final URI uri,
    final JsonParser parser)
    throws IOException, AOParseException
  {
    Preconditions.checkPrecondition(
      parser.currentToken() == START_OBJECT,
      "Parser must be at object start");

    final AORelease.Builder builder = AORelease.builder();

    while (true) {
      final JsonToken next = parser.nextToken();
      LOG.trace("{}: token: {}", uri, next);
      this.requireNotEOF(uri, parser, next);

      this.requireOneOf(uri, parser, next, FIELD_NAME, END_OBJECT);
      if (next == END_OBJECT) {
        break;
      }

      final String field_name = parser.getText();
      switch (field_name) {
        case "release_name": {
          parser.nextToken();
          builder.setName(parser.getText());
          break;
        }
        case "timestamp": {
          parser.nextToken();
          builder.setTime(ZonedDateTime.parse(parser.getText()));
          break;
        }
        case "binaries": {
          parser.nextToken();
          builder.setBinaries(this.parseBinariesArray(uri, parser));
          break;
        }
        default: {
          LOG.warn("ignoring unrecognized field: {}", field_name);
          parser.nextToken();
          parser.skipChildren();
        }
      }
    }

    try {
      return builder.build();
    } catch (final IllegalStateException e) {
      throw this.parseErrorMissingFields(uri, parser, e);
    }
  }

  /**
   * Parse an array of binaries from the given parser.
   */

  private List<AOBinary> parseBinariesArray(
    final URI uri,
    final JsonParser parser)
    throws IOException, AOParseException
  {
    Preconditions.checkPrecondition(
      parser.currentToken() == START_ARRAY,
      "Parser must be at array start");

    final List<AOBinary> binaries = new ArrayList<>();
    while (true) {
      final JsonToken next = parser.nextToken();
      LOG.trace("{}: token: {}", uri, next);
      this.requireNotEOF(uri, parser, next);

      this.requireOneOf(uri, parser, next, START_OBJECT, END_ARRAY);
      if (next == END_ARRAY) {
        return binaries;
      }

      binaries.add(this.parseBinary(uri, parser));
    }
  }

  /**
   * Parse a single binary from the given parser.
   */

  private AOBinary parseBinary(
    final URI uri,
    final JsonParser parser)
    throws IOException, AOParseException
  {
    Preconditions.checkPrecondition(
      parser.currentToken() == START_OBJECT,
      "Parser must be at object start");

    final AOBinary.Builder builder = AOBinary.builder();

    while (true) {
      final JsonToken next = parser.nextToken();
      LOG.trace("{}: token: {}", uri, next);
      this.requireNotEOF(uri, parser, next);

      this.requireOneOf(uri, parser, next, FIELD_NAME, END_OBJECT);
      if (next == END_OBJECT) {
        break;
      }

      final String field_name = parser.getText();
      switch (field_name) {
        case "platform": {
          parser.nextToken();
          this.parseOperatingSystemAndArchitecture(uri, parser, builder);
          break;
        }

        case "binary_name": {
          parser.nextToken();
          builder.setName(parser.getText());
          break;
        }

        case "binary_link": {
          parser.nextToken();
          builder.setLink(this.parseURI(uri, parser, parser.getText()));
          break;
        }

        case "binary_size": {
          parser.nextToken();
          builder.setSize(this.parseSize(uri, parser, parser.getText()));
          break;
        }

        case "checksum_link": {
          parser.nextToken();
          builder.setChecksumLink(this.parseURI(uri, parser, parser.getText()));
          break;
        }

        default: {
          LOG.warn("ignoring unrecognized field: {}", field_name);
          parser.nextToken();
          parser.skipChildren();
        }
      }
    }

    try {
      return builder.build();
    } catch (final IllegalStateException e) {
      throw this.parseErrorMissingFields(uri, parser, e);
    }
  }

  private AOParseException parseErrorMissingFields(
    final URI uri,
    final JsonParser parser,
    final IllegalStateException e)
  {
    return new AOParseException(
      MessageFormat.format(
        this.messages.getString("json_missing_fields"),
        formatLocation(uri, parser.getCurrentLocation()),
        e.getMessage()));
  }

  /**
   * Parse a size value such as "100 MB"
   */

  private BigInteger parseSize(
    final URI uri,
    final JsonParser parser,
    final String text)
    throws AOParseException
  {
    final Matcher matcher = SIZE_MEGABYTES.matcher(text);
    if (matcher.matches()) {
      return new BigInteger(matcher.group(1))
        .multiply(new BigInteger("1000000"));
    }

    throw this.parseErrorExpectedReceived(
      uri,
      parser,
      this.messages.getString("json_size_bytes"),
      text);
  }

  /**
   * Parse a URI
   */

  private URI parseURI(
    final URI uri,
    final JsonParser parser,
    final String text)
    throws AOParseException
  {
    try {
      return new URI(text);
    } catch (final URISyntaxException e) {
      throw this.parseErrorExpectedReceived(
        Optional.of(e),
        uri,
        parser,
        this.messages.getString("json_valid_uri"),
        text);
    }
  }

  /**
   * Parse a string such as "Linux ppc64le" into a separate operating system
   * and architecture string.
   */

  private void parseOperatingSystemAndArchitecture(
    final URI uri,
    final JsonParser parser,
    final AOBinary.Builder builder)
    throws IOException, AOParseException
  {
    Preconditions.checkPrecondition(
      parser.currentToken() == VALUE_STRING,
      "Parser must be at string value start");

    final String text = parser.getText();
    final List<String> segments = List.of(WHITESPACE.split(text.trim()));
    if (segments.size() != 2) {
      throw this.parseErrorPlatformArchitecture(uri, parser, text);
    }

    builder.setOperatingSystem(segments.get(0));
    builder.setArchitecture(segments.get(1));
  }

  private AOParseException parseErrorPlatformArchitecture(
    final URI uri,
    final JsonParser parser,
    final String text)
  {
    return new AOParseException(MessageFormat.format(
      this.messages.getString("json_platform_parse_error"),
      formatLocation(uri, parser.getCurrentLocation()),
      text));
  }

  private AOParseException parseErrorExpectedReceived(
    final Optional<Exception> cause,
    final URI uri,
    final JsonParser parser,
    final String expected,
    final String received)
  {
    final String pattern =
      this.messages.getString("json_parse_error");

    final Object[] arguments = {
      formatLocation(uri, parser.getCurrentLocation()),
      expected,
      received,
    };

    return cause.map(
      ex -> new AOParseException(MessageFormat.format(pattern, arguments)))
      .orElse(new AOParseException(MessageFormat.format(pattern, arguments)));
  }

  private AOParseException parseErrorExpectedReceived(
    final URI uri,
    final JsonParser parser,
    final String expected,
    final String received)
  {
    return this.parseErrorExpectedReceived(
      Optional.empty(), uri, parser, expected, received);
  }
}
