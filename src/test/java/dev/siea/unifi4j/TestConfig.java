package dev.siea.unifi4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads test config from environment variables, with optional override from a .env file.
 * Use .env.example as a template; copy to .env and never commit .env.
 */
public final class TestConfig {

    public static final String UNIFI_API_KEY = "UNIFI_API_KEY";
    public static final String UNIFI_BASE_URI = "UNIFI_BASE_URI";
    public static final String UNIFI_ALLOW_INSECURE_SSL = "UNIFI_ALLOW_INSECURE_SSL";

    private static final Map<String, String> ENV_OVERRIDES = loadEnvFile();

    private static Map<String, String> loadEnvFile() {
        Map<String, String> map = new HashMap<>();
        for (Path candidate : new Path[]{
                Paths.get(".env"),
                Paths.get(System.getProperty("user.dir", ".")).resolve(".env")
        }) {
            if (Files.isRegularFile(candidate)) {
                try {
                    Files.readAllLines(candidate).stream()
                            .map(String::trim)
                            .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                            .forEach(line -> {
                                int eq = line.indexOf('=');
                                if (eq > 0) {
                                    String key = line.substring(0, eq).trim();
                                    String value = line.substring(eq + 1).trim();
                                    if (!key.isEmpty()) map.put(key, value);
                                }
                            });
                } catch (IOException ignored) {
                }
                break;
            }
        }
        return map;
    }

    private static String get(String key) {
        if (ENV_OVERRIDES.containsKey(key)) {
            return ENV_OVERRIDES.get(key);
        }
        return System.getenv(key);
    }

    public static String apiKey() {
        return get(UNIFI_API_KEY);
    }

    public static String baseUri() {
        return get(UNIFI_BASE_URI);
    }

    public static boolean allowInsecureSsl() {
        String v = get(UNIFI_ALLOW_INSECURE_SSL);
        return "true".equalsIgnoreCase(v) || "1".equals(v);
    }

    /** Returns true if enough config is set to run integration tests. */
    public static boolean isConfigured() {
        String key = apiKey();
        String uri = baseUri();
        return key != null && !key.isBlank() && uri != null && !uri.isBlank();
    }

    private TestConfig() {
    }
}
