openjdk-api-java-client
===

[![Build Status](https://img.shields.io/travis/AdoptOpenJDK/openjdk-api-java-client.svg?style=flat-square)](https://travis-ci.org/AdoptOpenJDK/openjdk-api-java-client)
[![Maven Central](https://img.shields.io/maven-central/v/net.adoptopenjdk/net.adoptopenjdk.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22net.adoptopenjdk%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/net.adoptopenjdk/net.adoptopenjdk.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/net/adoptopenjdk/)
[![Codacy Badge](https://img.shields.io/codacy/grade/565ef1e0d8404f6b9cd22ef71fc73e48.svg?style=flat-square)](https://www.codacy.com/app/github_79/AdoptOpenJDK-Java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=AdoptOpenJDK/openjdk-api-java-client&amp;utm_campaign=Badge_Grade)
[![Codecov](https://img.shields.io/codecov/c/github/AdoptOpenJDK/openjdk-api-java-client.svg?style=flat-square)](https://codecov.io/gh/AdoptOpenJDK/openjdk-api-java-client)

A Java client for the [AdoptOpenJDK REST API](https://api.adoptopenjdk.net/).

![adoptopenjdk](./src/site/resources/adoptopenjdk.jpg?raw=true)

## Features

* Efficient, type-safe access to the AdoptOpenJDK API
* Clean API/implementation separation for easy API mocking in applications
* [JPMS](https://openjdk.java.net/projects/jigsaw/spec/)-ready
* [OSGi](https://www.osgi.org)-ready
* High coverage automated test suite
* Apache 2.0 license
* Fully documented (JavaDOC)

## Usage

Use the following Maven dependencies:

```
<dependency>
  <groupId>net.adoptopenjdk</groupId>
  <artifactId>net.adoptopenjdk.v3.api</artifactId>
  <version><!-- Insert latest version --></version>
</dependency>
<dependency>
  <groupId>net.adoptopenjdk</groupId>
  <artifactId>net.adoptopenjdk.v3.vanilla</artifactId>
  <version><!-- Insert latest version --></version>
</dependency>
```

The first dependency specifies that you want to use the API, and the second
is a basic provider for the API.

Then:

```
var clients = new AOV3Clients();
try (var client = clients.createClient()) {
  var request = client.availableReleases(...);
  var releases = request.execute();
}
```

The API operates entirely synchronously and raises checked exceptions on
failures.

The `net.adoptopenjdk.v3.api.AOV3ClientProviderType` interface is published
both as a JPMS service and an [OSGi service](https://www.osgi.org) in order to 
allow for decoupling consumers from the `vanilla` implementation package:

```
var clients =
  ServiceLoader.load(AOV3ClientProviderType.class)
    .findFirst()
    .orElseThrow(() -> new IllegalStateException(
      String.format("No implementations of %s are available", AOV3ClientProviderType.class)));

try (var client = clients.createClient()) {
  var request = client.availableReleases(...);
  var releases = request.execute();
}
```
