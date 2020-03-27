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

package net.adoptopenjdk.v3.vanilla;

import net.adoptopenjdk.v3.api.AOV3AvailableReleases;
import net.adoptopenjdk.v3.api.AOV3Release;
import net.adoptopenjdk.v3.api.AOV3VersionData;

import java.io.IOException;
import java.util.List;

/**
 * A response data parser.
 */

public interface AOV3ResponseParserType
{
  AOV3AvailableReleases parseAvailableReleases()
    throws IOException;

  List<String> parseReleaseNames()
    throws IOException;

  List<AOV3VersionData> parseReleaseVersions()
    throws IOException;

  List<AOV3Release> parseAssetsForRelease()
    throws IOException;
}
