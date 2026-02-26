package dev.siea.unifi4j.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.siea.unifi4j.async.UnifiAction;
import dev.siea.unifi4j.exception.*;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class UnifiHttpClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Logger logger;
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;

    public UnifiHttpClient(String baseUrl, String apiKey, boolean allowInsecureSsl, Logger logger) {
        this.logger = logger;
        this.baseUrl = (baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl) + "/proxy/network/integration/v1/";
        this.apiKey = apiKey;
        if (allowInsecureSsl) {
            logger.info("Creating insecure HTTP client");
            this.httpClient = createInsecureClient();
        } else {
            logger.info("Creating secure HTTP client");
            this.httpClient = HttpClient.newHttpClient();
        }

    }

    public UnifiAction<String> getAsync(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("X-API-KEY", apiKey)
                .GET()
                .build();

        CompletableFuture<String> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int statusCode = response.statusCode();
                    String body = response.body();

                    logger.debug("GET " + path + " responded with " + statusCode + ": " + body);

                    switch (statusCode) {
                        case 200, 201:
                            return body;
                        case 401, 403:
                            throw new CompletionException(new AuthenticationException("Authentication failed: " + body));
                        case 429:
                            throw new CompletionException(new RateLimitException("Rate limit exceeded: " + body));
                        default:
                            throw new CompletionException(new ApiException("API error " + statusCode + ": " + body));
                    }
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    throw new CompletionException(new NetworkException("Network error while calling " + path, cause));
                });

        return new UnifiAction<>(future);
    }

    /**
     * GET the given path and deserialize the JSON response into the given type.
     */
    public <T> UnifiAction<T> getAsync(String path, Class<T> responseType) {
        return getAsync(path).thenApply(body -> {
            try {
                return OBJECT_MAPPER.readValue(body, responseType);
            } catch (JsonProcessingException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * POST the given path with JSON body and return the raw response body.
     */
    public UnifiAction<String> postAsync(String path, Object body) {
        String bodyJson;
        try {
            bodyJson = body != null ? OBJECT_MAPPER.writeValueAsString(body) : "{}";
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("X-API-KEY", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();

        CompletableFuture<String> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int statusCode = response.statusCode();
                    String responseBody = response.body();
                    logger.debug("POST " + path + " responded with " + statusCode + ": " + responseBody);
                    switch (statusCode) {
                        case 200, 201:
                            return responseBody;
                        case 401, 403:
                            throw new CompletionException(new AuthenticationException("Authentication failed: " + responseBody));
                        case 429:
                            throw new CompletionException(new RateLimitException("Rate limit exceeded: " + responseBody));
                        default:
                            throw new CompletionException(new ApiException("API error " + statusCode + ": " + responseBody));
                    }
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    throw new CompletionException(new NetworkException("Network error while calling " + path, cause));
                });
        return new UnifiAction<>(future);
    }

    /**
     * POST the given path with JSON body and deserialize the response into the given type.
     */
    public <T> UnifiAction<T> postAsync(String path, Object body, Class<T> responseType) {
        return postAsync(path, body).thenApply(responseBody -> {
            if (responseBody == null || responseBody.isBlank()) {
                return null;
            }
            try {
                return OBJECT_MAPPER.readValue(responseBody, responseType);
            } catch (JsonProcessingException e) {
                throw new CompletionException(e);
            }
        });
    }

    private HttpClient createInsecureClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            SSLParameters sslParams = sslContext.getDefaultSSLParameters();
            sslParams.setEndpointIdentificationAlgorithm(null);

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .sslParameters(sslParams)
                    .build();

        } catch (Exception e) {
            logger.warn("Unable to create insecure HTTP Client. Using secure client as fallback... (Enable Debug for full error)");
            logger.debug(String.valueOf(e));
            return HttpClient.newHttpClient();
        }
    }
}
