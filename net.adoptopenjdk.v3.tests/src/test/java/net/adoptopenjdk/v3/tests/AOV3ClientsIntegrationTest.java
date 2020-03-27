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

import net.adoptopenjdk.v3.api.AOV3Architecture;
import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3HeapSize;
import net.adoptopenjdk.v3.api.AOV3ImageKind;
import net.adoptopenjdk.v3.api.AOV3JVMImplementation;
import net.adoptopenjdk.v3.api.AOV3OperatingSystem;
import net.adoptopenjdk.v3.api.AOV3Release;
import net.adoptopenjdk.v3.api.AOV3ReleaseKind;
import net.adoptopenjdk.v3.api.AOV3SortOrder;
import net.adoptopenjdk.v3.api.AOV3Vendor;
import net.adoptopenjdk.v3.api.AOV3VersionBound;
import net.adoptopenjdk.v3.api.AOV3VersionRange;
import net.adoptopenjdk.v3.vanilla.AOV3Clients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AOV3ClientsIntegrationTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV3ClientsIntegrationTest.class);

  private AOV3Clients clients;
  private ArrayList<AOV3Error> errors;

  @BeforeEach
  public void testSetup()
  {
    this.clients = new AOV3Clients();
    this.errors = new ArrayList<AOV3Error>();
  }

  private void logError(
    final AOV3Error error)
  {
    LOG.error("error: {}", error);
    this.errors.add(error);
  }

  @Test
  public void testAvailableReleases()
    throws Exception
  {
    try (var client = this.clients.createClient()) {
      final var request = client.availableReleases(this::logError);
      LOG.debug("releases: {}", request.execute());
    }
  }

  @Test
  public void testReleaseNames()
    throws Exception
  {
    try (var client = this.clients.createClient()) {
      final var request = client.releaseNames(
        this::logError,
        BigInteger.ZERO,
        BigInteger.TEN,
        Optional.of(AOV3ReleaseKind.GENERAL_AVAILABILITY),
        Optional.of(AOV3SortOrder.ASCENDING),
        Optional.of(AOV3Vendor.ADOPT_OPENJDK),
        Optional.of(AOV3VersionRange.of(
          AOV3VersionBound.of(Optional.of("11"), false),
          AOV3VersionBound.of(Optional.of("12"), true)
        ))
      );

      final var releases = request.execute();
      LOG.debug("releases: {}", Integer.valueOf(releases.size()));
      LOG.debug("releases: {}", releases);
    }
  }

  @Test
  public void testReleaseVersions()
    throws Exception
  {
    try (var client = this.clients.createClient()) {
      final var request = client.releaseVersions(
        this::logError,
        BigInteger.ZERO,
        BigInteger.TEN,
        Optional.of(AOV3ReleaseKind.GENERAL_AVAILABILITY),
        Optional.of(AOV3SortOrder.ASCENDING),
        Optional.of(AOV3Vendor.ADOPT_OPENJDK),
        Optional.of(AOV3VersionRange.of(
          AOV3VersionBound.of(Optional.of("11"), false),
          AOV3VersionBound.of(Optional.of("12"), true)
        ))
      );

      final var releases = request.execute();
      LOG.debug("releases: {}", Integer.valueOf(releases.size()));
      LOG.debug("releases: {}", releases);
    }
  }

  @Test
  public void testAssetsForRelease()
    throws Exception
  {
    try (var client = this.clients.createClient()) {
      final var request = client.assetsForRelease(
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
      );

      final var releases = request.execute();
      LOG.debug("releases: {}", Integer.valueOf(releases.size()));
      LOG.debug("releases: {}", releases);
    }
  }

  @Test
  public void testAssetsForReleaseFiltered()
    throws Exception
  {
    try (var client = this.clients.createClient()) {
      final var request = client.assetsForRelease(
        this::logError,
        BigInteger.ZERO,
        BigInteger.TEN,
        BigInteger.valueOf(11L),
        AOV3ReleaseKind.GENERAL_AVAILABILITY,
        Optional.of(AOV3Architecture.X64),
        Optional.of(AOV3HeapSize.NORMAL),
        Optional.of(AOV3ImageKind.JDK),
        Optional.of(AOV3JVMImplementation.HOTSPOT),
        Optional.of(AOV3OperatingSystem.LINUX),
        Optional.of("jdk"),
        Optional.of(AOV3SortOrder.DESCENDING),
        Optional.of(AOV3Vendor.ADOPT_OPENJDK)
      );

      final var releases = request.execute();
      LOG.debug("releases: {}", Integer.valueOf(releases.size()));
      LOG.debug("releases: {}", releases);
    }
  }
}
