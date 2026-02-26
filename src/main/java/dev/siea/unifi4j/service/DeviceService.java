package dev.siea.unifi4j.service;

import dev.siea.unifi4j.async.UnifiAction;
import dev.siea.unifi4j.client.UnifiHttpClient;
import dev.siea.unifi4j.model.DevicesQuery;
import dev.siea.unifi4j.model.DevicesResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeviceService extends UnifiService {

    private static final String DEVICES_PATH_TEMPLATE = "sites/%s/devices";

    public DeviceService(UnifiHttpClient client) {
        super(client);
    }

    /** Fetches all devices in the site with default pagination (no query params). */
    public UnifiAction<DevicesResponse> getDevices(@NotNull String siteId) {
        return getDevices(siteId, null);
    }

    /** Fetches devices in the site with optional pagination and filter. */
    public UnifiAction<DevicesResponse> getDevices(@NotNull String siteId, @Nullable DevicesQuery query) {
        String path = String.format(DEVICES_PATH_TEMPLATE, siteId);
        if (query != null) {
            String qs = query.toQueryString();
            if (!qs.isEmpty()) {
                path = path + qs;
            }
        }
        return client.getAsync(path, DevicesResponse.class);
    }
}
