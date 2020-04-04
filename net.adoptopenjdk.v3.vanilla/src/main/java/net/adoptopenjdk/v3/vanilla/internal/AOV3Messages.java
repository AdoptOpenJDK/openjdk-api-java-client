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

import java.net.URI;
import java.net.http.HttpClient;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class AOV3Messages implements AOV3MessagesType
{
  private final ResourceBundle resourceBundle;

  private AOV3Messages(
    final ResourceBundle inResourceBundle)
  {
    this.resourceBundle = inResourceBundle;
  }

  /**
   * Retrieve the resource bundle for the given locale.
   *
   * @param locale The locale
   *
   * @return The resource bundle
   */

  public static ResourceBundle getResourceBundle(
    final Locale locale)
  {
    return ResourceBundle.getBundle(
      "net.adoptopenjdk.v3.vanilla.internal.Strings",
      locale);
  }

  /**
   * Retrieve the resource bundle for the current locale.
   *
   * @return The resource bundle
   */

  public static ResourceBundle getResourceBundle()
  {
    return getResourceBundle(Locale.getDefault());
  }

  /**
   * Create a new string provider from the given bundle.
   *
   * @param bundle The resource bundle
   *
   * @return A string provider
   */

  public static AOV3MessagesType of(
    final ResourceBundle bundle)
  {
    return new AOV3Messages(bundle);
  }

  @Override
  public ResourceBundle resourceBundle()
  {
    return this.resourceBundle;
  }

  @Override
  public String format(
    final String id,
    final Object... args)
  {
    return MessageFormat.format(this.resourceBundle.getString(id), args);
  }

  @Override
  public String httpClientNoRedirects(
    final HttpClient.Redirect followRedirects)
  {
    return this.format("http.clientMisconfigured", followRedirects);
  }

  @Override
  public String locationMissing(
    final int statusCode,
    final URI uri)
  {
    return this.format("http.locationMissing", uri, Integer.valueOf(statusCode));
  }

  @Override
  public String requestFailed(
    final int statusCode,
    final URI uri)
  {
    return this.format("http.requestFailed", uri, Integer.valueOf(statusCode));
  }
}
