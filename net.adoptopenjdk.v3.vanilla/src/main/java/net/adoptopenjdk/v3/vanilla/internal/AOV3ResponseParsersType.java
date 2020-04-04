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

package net.adoptopenjdk.v3.vanilla.internal;

import net.adoptopenjdk.v3.api.AOV3Error;

import java.io.InputStream;
import java.net.URI;
import java.util.function.Consumer;

/**
 * A provider of response parsers.
 */

public interface AOV3ResponseParsersType
{
  AOV3ResponseParserType createParser(
    Consumer<AOV3Error> errorReceiver,
    URI source,
    InputStream stream);
}
