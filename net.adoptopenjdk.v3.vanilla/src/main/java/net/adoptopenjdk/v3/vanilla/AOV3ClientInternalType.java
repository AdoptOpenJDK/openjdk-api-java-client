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

import net.adoptopenjdk.v3.api.AOV3Error;
import net.adoptopenjdk.v3.api.AOV3Exception;

import java.net.URI;
import java.util.function.Consumer;

public interface AOV3ClientInternalType
{
  default AOV3ResponseParserType parserFor(
    final Consumer<AOV3Error> errorReceiver,
    final String sourceURI)
    throws AOV3Exception, InterruptedException
  {
    return this.parserForURI(errorReceiver, URI.create(sourceURI));
  }

  AOV3ResponseParserType parserForURI(
    Consumer<AOV3Error> errorReceiver,
    URI sourceURI)
    throws AOV3Exception, InterruptedException;

  String baseURI();
}
