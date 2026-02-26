package dev.siea.unifi4j.exception;

public class RateLimitException extends UnifiException {
    public RateLimitException(String message) {
        super(message);
    }
}
