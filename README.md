# Unifi4J - Unifi for Java

[![Documentation](https://img.shields.io/badge/docs-available-brightgreen)](https://docs.siea.dev/unifi4j/)

Java client for the **Unifi Network** API with a reactive-style API.

**Requirements:** Java 21+

## About

Unifi4J lets you talk to a Unifi Network controller from Java. You configure it with an API key and controller URL, then use services to call functions. All calls return `UnifiAction<T>` (backed by `CompletableFuture<T>`), so you can block with `.complete()`, use callbacks with `.queue()`, or chain async work with `.thenCompose()`. The client uses the official Unifi Network integration API and maps errors to typed exceptions (auth, rate limit, API, network). Full docs cover installation, configuration (including when to use insecure SSL), each service, and the reactive API.

## Installation

```xml
<dependency>
    <groupId>dev.siea.unifi4j</groupId>
    <artifactId>unifi4j</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
