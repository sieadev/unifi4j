# Unifi4J

Java client for the **Unifi Network** API with a reactive-style API. Typed responses, optional insecure SSL for self-hosted controllers, and a simple service registry.

**Requirements:** Java 21+

---

## Installation

Add the dependency to your project (Maven):

```xml
<dependency>
    <groupId>dev.siea.unifi4j</groupId>
    <artifactId>unifi4j</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Or build from source:

```bash
mvn clean install
```

---

## Quick start

```java
Unifi4J unifi = Unifi4J.withApiKey("your-api-key")
        .withBaseUri("https://192.168.1.1/")
        .allowInsecureSsl(true)   // for self-signed / IP-based controllers
        .build();

// Blocking
NetworkInfo info = unifi.network().getInfo().complete();
System.out.println("Version: " + info.getApplicationVersion());

// Async callback
unifi.network().getSites().queue(
    sites -> System.out.println("Sites: " + sites.getTotalCount()),
    err -> System.err.println("Failed: " + err)
);
```

---

## Configuration

| Method | Description |
|--------|-------------|
| `withApiKey(String)` | API key (required). |
| `withBaseUri(String)` | Base URL of the Unifi Network API (e.g. `https://192.168.1.1/`). |
| `allowInsecureSsl(boolean)` | Accept self-signed certs and skip hostname verification (e.g. when using an IP). |

You must call `build()` before using any service. The client checks connectivity to the API during `build()`.

---

## Services

Access services by type or via shortcuts:

```java
// By class (scales to many services)
NetworkService network = unifi.getService(NetworkService.class);
DeviceService device = unifi.getService(DeviceService.class);

// Shortcuts
NetworkService network = unifi.network();
DeviceService device = unifi.device();
```

### Network service

- **`getInfo()`** → `UnifiAction<NetworkInfo>` — application version.
- **`getSites()`** → `UnifiAction<SitesResponse>` — all sites (default pagination).
- **`getSites(SitesQuery query)`** — sites with offset, limit, and filters.

```java
// Pagination and filter
SitesQuery query = SitesQuery.builder()
        .offset(0)
        .limit(20)
        .filter(SiteFilterField.NAME, SiteFilterOperator.EQ, "Office")
        .build();
SitesResponse sites = unifi.network().getSites(query).complete();
```

### Device service

- **`getDevices(String siteId)`** → `UnifiAction<DevicesResponse>` — all devices in the site.
- **`getDevices(String siteId, DevicesQuery query)`** — with offset, limit, and filter.

```java
DevicesQuery query = DevicesQuery.builder()
        .offset(0)
        .limit(50)
        .filter("state:eq:ONLINE")
        .build();
DevicesResponse devices = unifi.device().getDevices(siteId, query).complete();
```

---

## Reactive API

All service methods return `UnifiAction<T>`, which wraps a `CompletableFuture<T>`:

| Method | Description |
|--------|-------------|
| `complete()` | Block until done; throws `UnifiException` on failure. |
| `queue(success, failure)` | Callback when done. |
| `thenApply(fn)` | Transform the result. |
| `thenCompose(fn)` | Chain another async action. |
| `toFuture()` | Get the underlying `CompletableFuture<T>`. |

```java
// Chain async calls
unifi.network().getSites()
        .thenCompose(sites -> {
            Site first = sites.getData().get(0);
            return unifi.device().getDevices(first.getId().toString());
        })
        .queue(
            devices -> System.out.println("Devices: " + devices.getTotalCount()),
            Throwable::printStackTrace
        );
```

