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

package net.adoptopenjdk.tests.api;

import net.adoptopenjdk.api.AdoptOpenJDK;
import net.adoptopenjdk.api.AdoptOpenJDKRequestsType;
import net.adoptopenjdk.spi.AOException;
import org.junit.Test;

public final class AdoptOpenJDKTest
{
  @Test
  public void testGetAndUse()
    throws AOException
  {
    final AdoptOpenJDKRequestsType requests = AdoptOpenJDK.get(1);
    requests.rateLimitRemaining();
  }
}
