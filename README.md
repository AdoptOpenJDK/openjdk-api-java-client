AdoptOpenJDK-Java
===

[![Build Status](https://img.shields.io/travis/io7m/AdoptOpenJDK-Java.svg?style=flat-square)](https://travis-ci.org/io7m/AdoptOpenJDK-Java)
[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.adoptopenjdk/com.io7m.adoptopenjdk.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.adoptopenjdk%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.io7m.adoptopenjdk/com.io7m.adoptopenjdk.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/com/io7m/adoptopenjdk/)
[![Codacy Badge](https://img.shields.io/codacy/grade/565ef1e0d8404f6b9cd22ef71fc73e48.svg?style=flat-square)](https://www.codacy.com/app/github_79/AdoptOpenJDK-Java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=io7m/AdoptOpenJDK-Java&amp;utm_campaign=Badge_Grade)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/AdoptOpenJDK-Java.svg?style=flat-square)](https://codecov.io/gh/io7m/AdoptOpenJDK-Java)

![adoptopenjdk](./src/site/resources/adoptopenjdk.jpg?raw=true)

Usage
===

Use the following Maven dependencies:

```
<dependency>
  <groupId>com.io7m.adoptopenjdk</groupId>
  <artifactId>com.io7m.adoptopenjdk.api</artifactId>
  <version><!-- Insert latest version --></version>
</dependency>
<dependency>
  <groupId>com.io7m.adoptopenjdk</groupId>
  <artifactId>com.io7m.adoptopenjdk.v1</artifactId>
  <version><!-- Insert latest version --></version>
</dependency>
```

The first dependency specifies that you want to use the API, and the second
is a basic provider for the version `1.0.0` API.

Then:

```
AdoptOpenJDKRequestsType requests = AdoptOpenJDK.get(1);

List<AOVariants> variants = requests.variants();
assert variants.size() > 0;

List<AORelease> releases =
  requests.releasesForVariant(variants.get(0).name());

List<AORelease> releases_8 =
  requests.releasesForVariant("openjdk8");
```

The API operates entirely synchronously and raises checked exceptions on
failures. Do not share values of type `AdoptOpenJDKRequestsType` across threads.
