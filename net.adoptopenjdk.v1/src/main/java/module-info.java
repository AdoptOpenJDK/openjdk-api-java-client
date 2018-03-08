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

import net.adoptopenjdk.spi.AOAPIVersionProviderType;
import net.adoptopenjdk.v1.AOv1Requests;

/**
 * Version 1.* API provider.
 */

module net.adoptopenjdk.v1
{
  requires static org.immutables.value;

  requires com.io7m.jaffirm.core;
  requires net.adoptopenjdk.spi;
  requires com.fasterxml.jackson.core;
  requires org.slf4j;

  exports net.adoptopenjdk.v1;

  provides AOAPIVersionProviderType with AOv1Requests.Provider;
}