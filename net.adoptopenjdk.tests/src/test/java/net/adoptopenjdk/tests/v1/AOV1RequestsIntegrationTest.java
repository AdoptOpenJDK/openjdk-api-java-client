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

import net.adoptopenjdk.spi.AOException;
import net.adoptopenjdk.spi.AORelease;
import net.adoptopenjdk.spi.AOVariant;
import net.adoptopenjdk.v1.AOv1HTTPException;
import net.adoptopenjdk.v1.AOv1Requests;
import net.adoptopenjdk.v1.AOv1RequestsType;
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
