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

import net.adoptopenjdk.v3.api.AOV3ClientProviderType;
import net.adoptopenjdk.v3.vanilla.AOV3Clients;

module net.adoptopenjdk.v3.vanilla
{
  requires static com.fasterxml.jackson.annotation;
  requires static org.osgi.annotation.bundle;
  requires static org.osgi.annotation.versioning;
  requires static org.osgi.service.component.annotations;

  requires transitive net.adoptopenjdk.v3.api;

  requires com.fasterxml.jackson.databind;
  requires java.net.http;
  requires org.slf4j;

  opens net.adoptopenjdk.v3.vanilla.internal to com.fasterxml.jackson.databind;

  exports net.adoptopenjdk.v3.vanilla;

  provides AOV3ClientProviderType with AOV3Clients;
}
