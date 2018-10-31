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

package net.adoptopenjdk.spi;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Style settings for generated immutable types.
 */

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
  get = {"is*", "get*"},
  init = "set*",
  typeAbstract = {"Abstract*", "*Type"},
  typeImmutable = "*",
  typeModifiable = "*Mutable",
  builder = "builder",
  build = "build",
  allowedClasspathAnnotations = Override.class,
  visibility = Value.Style.ImplementationVisibility.PUBLIC,
  defaults = @Value.Immutable(copy = false))
public @interface ImmutableStyleType
{
  // No value-level representation
}
