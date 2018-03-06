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

package com.io7m.adoptopenjdk.tests.v1;

import com.io7m.adoptopenjdk.spi.AOException;
import com.io7m.adoptopenjdk.spi.AORelease;
import com.io7m.adoptopenjdk.spi.AOVariant;
import com.io7m.adoptopenjdk.v1.AOv1HTTPException;
import com.io7m.adoptopenjdk.v1.AOv1Requests;
import com.io7m.adoptopenjdk.v1.AOv1RequestsType;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class AOV1RequestsIntegrationTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV1RequestsIntegrationTest.class);

  @Test
  public void testOpen()
    throws AOException
  {
    try {
      final AOv1RequestsType requests = AOv1Requests.open();
      Assert.assertTrue(
        "Must detect the right number of remaining requests",
        (long) requests.rateLimitRemaining() >= 0L);
    } catch (final AOv1HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseOpenJDK8Releases()
    throws AOException
  {
    try {
      final AOv1RequestsType requests = AOv1Requests.open();
      final List<AORelease> releases = requests.releasesForVariant("openjdk8");
      Assert.assertTrue(
        "Must have at least one release parsed",
        releases.size() > 0);
    } catch (final AOv1HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseOpenJDK8Nightlies()
    throws AOException
  {
    try {
      final AOv1RequestsType requests = AOv1Requests.open();
      final List<AORelease> releases =
        requests.nightlyBuildsForVariant("openjdk8");
      Assert.assertTrue(
        "Must have at least one release parsed",
        releases.size() > 0);
    } catch (final AOv1HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseVariants()
    throws AOException
  {
    try {
      final AOv1RequestsType requests = AOv1Requests.open();
      final List<AOVariant> variants = requests.variants();
      Assert.assertTrue(
        "Must have at least one variant parsed",
        variants.size() > 0);
    } catch (final AOv1HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  private static void checkAcceptableHTTPException(AOv1HTTPException e)
    throws AOv1HTTPException
  {
    if (e.statusCode() == 429) {
      LOG.info("exceeded rate limit on server: ", e);
      return;
    }
    if (e.statusCode() == 521) {
      LOG.info("server failure: ", e);
      return;
    }
    if (e.statusCode() == 520) {
      LOG.info("server failure: ", e);
      return;
    }
    throw e;
  }
}
