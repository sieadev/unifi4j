package dev.siea.unifi4j.service;

import dev.siea.unifi4j.async.UnifiAction;
import dev.siea.unifi4j.client.UnifiHttpClient;
import dev.siea.unifi4j.model.NetworkInfo;
import dev.siea.unifi4j.model.site.SitesQuery;
import dev.siea.unifi4j.model.site.SitesResponse;
import org.jetbrains.annotations.Nullable;

public class NetworkService extends UnifiService {
    private static final String INFO_PATH = "info";
    private static final String SITES_PATH = "sites";

    public NetworkService(UnifiHttpClient client) {
        super(client);
    }

    /** Fetches network integration info (e.g. application version). */
    public UnifiAction<NetworkInfo> getInfo() {
        return client.getAsync(INFO_PATH, NetworkInfo.class);
    }

    /** Fetches all sites with default pagination (no query params). */
    public UnifiAction<SitesResponse> getSites() {
        return getSites(null);
    }

    /** Fetches sites with optional pagination and filters. */
    public UnifiAction<SitesResponse> getSites(@Nullable SitesQuery query) {
        String path = SITES_PATH;
        if (query != null) {
            String qs = query.toQueryString();
            if (!qs.isEmpty()) {
                path = path + qs;
            }
        }
        return client.getAsync(path, SitesResponse.class);
    }
}
