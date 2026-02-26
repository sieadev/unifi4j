package dev.siea.unifi4j.exception;

public class UnifiException extends RuntimeException {
    public UnifiException(String message) {
        super(message);
    }

    public UnifiException(String message, Throwable cause) {
        super(message, cause);
    }
}

