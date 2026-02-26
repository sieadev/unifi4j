package dev.siea.unifi4j.service;

import dev.siea.unifi4j.client.UnifiHttpClient;

public abstract class UnifiService {
    protected final UnifiHttpClient client;

    protected UnifiService(UnifiHttpClient client) {
        this.client = client;
    }
}
