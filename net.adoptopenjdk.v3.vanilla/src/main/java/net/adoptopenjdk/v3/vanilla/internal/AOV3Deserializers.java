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

package net.adoptopenjdk.v3.vanilla.internal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * A deserializer that only allows for deserializing a fixed set of classes,
 * for reasons of security.
 */

final class AOV3Deserializers extends SimpleDeserializers
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AOV3Deserializers.class);

  private final Set<String> whitelist;

  private AOV3Deserializers(
    final Set<String> inWhitelist)
  {
    this.whitelist =
      Objects.requireNonNull(inWhitelist, "whitelist");
  }

  private static Set<String> classWhitelist()
  {
    return Set.of(
      "java.lang.String",
      "java.math.BigInteger",
      "java.net.URI",
      "java.util.List<java.lang.String>",
      "java.util.List<java.math.BigInteger>",
      "java.util.List<net.adoptopenjdk.v3.vanilla.internal.AOV3AST$AOV3BinaryJSON>",
      "java.util.List<net.adoptopenjdk.v3.vanilla.internal.AOV3AST$AOV3ListBinaryAssetViewJSON>",
      "java.util.List<net.adoptopenjdk.v3.vanilla.internal.AOV3AST$AOV3ReleaseJSON>",
      "java.util.List<net.adoptopenjdk.v3.vanilla.internal.AOV3AST$AOV3ReleaseVersionJSON>",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3AvailableReleasesJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3BinaryJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3InstallerJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3ListBinaryAssetViewJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3PackageJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3ReleaseJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3ReleaseNamesJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3ReleaseVersionJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3ReleaseVersionsJSON",
      "net.adoptopenjdk.v3.vanilla.internal.AOV3AST.AOV3SourceJSON"
    );
  }

  public static AOV3Deserializers create()
  {
    final Set<String> whiteList = classWhitelist();
    for (final var entry : whiteList) {
      LOG.trace("whitelist: {}", entry);
    }
    return new AOV3Deserializers(whiteList);
  }

  @Override
  public JsonDeserializer<?> findArrayDeserializer(
    final ArrayType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(type.toCanonical());
    return super.findArrayDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  private void checkWhitelist(final String name)
  {
    LOG.trace("checkWhitelist: {}", name);

    if (!this.whitelist.contains(name)) {
      throw new IllegalArgumentException(
        String.format("Deserializing a value of type %s is not allowed", name));
    }
  }

  @Override
  public JsonDeserializer<?> findBeanDeserializer(
    final JavaType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkWhitelist(type.getRawClass().getCanonicalName());
    return super.findBeanDeserializer(type, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findCollectionDeserializer(
    final CollectionType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(type.toCanonical());
    return super.findCollectionDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findCollectionLikeDeserializer(
    final CollectionLikeType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(type.toCanonical());
    return super.findCollectionLikeDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findEnumDeserializer(
    final Class<?> type,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkWhitelist(type.getCanonicalName());
    return super.findEnumDeserializer(type, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findTreeNodeDeserializer(
    final Class<? extends JsonNode> nodeType,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkWhitelist(nodeType.getCanonicalName());
    return super.findTreeNodeDeserializer(nodeType, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findReferenceDeserializer(
    final ReferenceType refType,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer contentTypeDeserializer,
    final JsonDeserializer<?> contentDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(refType.toCanonical());
    return super.findReferenceDeserializer(
      refType,
      config,
      beanDesc,
      contentTypeDeserializer,
      contentDeserializer);
  }

  @Override
  public JsonDeserializer<?> findMapDeserializer(
    final MapType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(type.toCanonical());
    return super.findMapDeserializer(
      type,
      config,
      beanDesc,
      keyDeserializer,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findMapLikeDeserializer(
    final MapLikeType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkWhitelist(type.toCanonical());
    return super.findMapLikeDeserializer(
      type,
      config,
      beanDesc,
      keyDeserializer,
      elementTypeDeserializer,
      elementDeserializer);
  }
}
