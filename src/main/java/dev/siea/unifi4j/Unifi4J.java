package dev.siea.unifi4j;

import dev.siea.unifi4j.client.UnifiHttpClient;
import dev.siea.unifi4j.service.DeviceService;
import dev.siea.unifi4j.service.NetworkService;
import dev.siea.unifi4j.service.UnifiService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Unifi4J {
    private final Logger logger = LogManager.getLogger(Unifi4J.class);
    private String apiKey;
    private String baseUri = "https://192.168.1.1/";
    private Map<Class<? extends UnifiService>, UnifiService> services;
    private boolean allowInsecureSsl = false;

    private Unifi4J() {

    }

    public static Unifi4J withApiKey(@NotNull String apiKey) {
        Unifi4J unifi4J = new Unifi4J();
        if (apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key must not be null or empty");
        }
        unifi4J.apiKey = apiKey;
        return unifi4J;
    }

    public Unifi4J withBaseUri(@NotNull String baseUri) {
        if (this.baseUri.isEmpty()) {
            throw new IllegalArgumentException("Base URL must not be null or empty");
        }
        this.baseUri = baseUri;
        return this;
    }

    public Unifi4J allowInsecureSsl(boolean allow) {
        this.allowInsecureSsl = allow;
        return this;
    }

    public Unifi4J build() {
        if (services != null) {
            logger.info("Unifi4J has already been build");
            return this;
        }

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key must not be null or empty");
        }

        logger.info("Building Unifi4J instance...");

        UnifiHttpClient client = new UnifiHttpClient(baseUri, apiKey, allowInsecureSsl, logger);
        this.services = new HashMap<>();
        register(new NetworkService(client));
        register(new DeviceService(client));

        try {
            String version = getService(NetworkService.class).getInfo().complete().getApplicationVersion();

            System.out.printf("""
                 
                 Unifi4J client built successfully!
                 ▖▖  ▘▐▘▘▖▖ ▖  | API Version : %s
                 ▌▌▛▌▌▜▘▌▙▌ ▌  | Base Url: %s
                 ▙▌▌▌▌▐ ▌ ▌▙▌  | Secure SSL: %s
                %n""", version, baseUri, !allowInsecureSsl);
        } catch (Exception e) {
            logger.error("Unable to establish a connection: " + e.getMessage());
        }

        return this;
    }

    private void register(UnifiService service) {
        services.put(service.getClass(), service);
    }

    /**
     * Returns the service instance for the given service type.
     *
     * @param serviceType the service class
     * @return the service instance
     * @throws IllegalStateException if build() has not been called
     * @throws IllegalArgumentException if the service type is not registered
     */
    @SuppressWarnings("unchecked")
    public <S extends UnifiService> S getService(Class<S> serviceType) {
        if (services == null) {
            throw new IllegalStateException("Unifi4J must be built first (call build())");
        }
        UnifiService service = services.get(serviceType);
        if (service == null) {
            throw new IllegalArgumentException("Unknown service type: " + serviceType.getName());
        }
        return (S) service;
    }

    public NetworkService network() {
        return getService(NetworkService.class);
    }

    public DeviceService device() {
        return getService(DeviceService.class);
    }
}