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

import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;
import net.adoptopenjdk.v2.AOv2HTTPConnections;
import net.adoptopenjdk.v2.AOv2HTTPException;
import net.adoptopenjdk.v2.AOv2HTTPProblemReport;
import net.adoptopenjdk.v2.AOv2Requests;
import net.adoptopenjdk.v2.AOv2RequestsType;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class AOV2RequestsIntegrationTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV2RequestsIntegrationTest.class);

  @Test
  public void testOpen()
    throws AOException
  {
    try {
      final AOv2RequestsType requests = AOv2Requests.open();
      Assert.assertTrue(
        "Must detect the right number of remaining requests",
        (long) requests.rateLimitRemaining() >= 0L);
    } catch (final AOv2HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseOpenJDK8Releases()
    throws AOException
  {
    try {
      final AOv2RequestsType requests = AOv2Requests.open();

      final List<AORelease> releases = requests.releasesForVariant("openjdk8");
      Assert.assertTrue(
        "Must have at least one release parsed",
        releases.size() > 0);
    } catch (final AOv2HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseOpenJDK10Nightlies()
    throws AOException
  {
    try {
      final AOv2RequestsType requests = AOv2Requests.open();
      final List<AORelease> releases =
        requests.nightlyBuildsForVariant("openjdk10");
      Assert.assertTrue(
        "Must have at least one release parsed",
        releases.size() > 0);
    } catch (final AOv2HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  @Test
  public void testParseVariants()
    throws AOException
  {
    try {
      final AOv2RequestsType requests = AOv2Requests.open();
      final List<AOVariant> variants = requests.variants();
      Assert.assertTrue(
        "Must have at least one variant parsed",
        variants.size() > 0);
    } catch (final AOv2HTTPException e) {
      checkAcceptableHTTPException(e);
    }
  }

  private static void checkAcceptableHTTPException(final AOv2HTTPException e)
    throws AOv2HTTPException
  {
    dumpErrorReport(e.report());

    if (e.statusCode() == 429) {
      LOG.info("exceeded rate limit on server: ", e);
      return;
    }
    if (e.statusCode() >= 500) {
      LOG.info("server failure: ", e);
      return;
    }
    throw e;
  }

  private static void dumpErrorReport(
    final AOv2HTTPProblemReport report)
  {
    try {
      final Path path =
        Files.createTempFile("net.adoptopenjdk.tests-error-", ".bin");
      LOG.info("writing error report to {}", path);
      Files.write(path, report.data());
    } catch (final IOException e) {
      LOG.error("could not save error report: ", e);
    }
  }
}
