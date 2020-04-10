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

package net.adoptopenjdk.v3.api;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.math.BigInteger;

/**
 * Versioning data.
 */

@ImmutablesStyleType
@Value.Immutable
public interface AOV3VersionDataType
{
  /**
   * @return The AdoptOpenJDK build number
   */

  BigInteger adoptBuildNumber();

  BigInteger build();

  /**
   * @return The major version number
   */

  BigInteger major();

  /**
   * @return The minor version number
   */

  BigInteger minor();

  String openJDKVersion();

  String optional();

  String pre();

  BigInteger security();

  /**
   * @return The semantic version number
   */

  String semanticVersion();
}
