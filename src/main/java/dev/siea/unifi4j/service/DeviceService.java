package dev.siea.unifi4j.service;

import dev.siea.unifi4j.async.UnifiAction;
import dev.siea.unifi4j.client.UnifiHttpClient;
import dev.siea.unifi4j.model.AdoptDeviceRequest;
import dev.siea.unifi4j.model.AdoptedDeviceAction;
import dev.siea.unifi4j.model.DevicesQuery;
import dev.siea.unifi4j.model.DevicesResponse;
import dev.siea.unifi4j.model.PortAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DeviceService extends UnifiService {

    private static final String DEVICES_PATH_TEMPLATE = "sites/%s/devices";
    private static final String ADOPT_PATH_TEMPLATE = "sites/%s/devices/adopt";
    private static final String PORT_ACTION_PATH_TEMPLATE = "sites/%s/devices/%s/interfaces/ports/%s/actions";
    private static final String ADOPTED_DEVICE_ACTION_PATH_TEMPLATE = "sites/%s/devices/%s/actions";

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

    /**
     * Adopts a device into the site.
     *
     * @param siteId   the site ID
     * @param request  macAddress (required) and ignoreDeviceLimit (required)
     * @return the raw response body (often empty on success)
     * @see <a href="https://developer.ui.com/network/v10.1.84/adoptdevice">Adopt Device</a>
     */
    public UnifiAction<String> adoptDevice(@NotNull String siteId, @NotNull AdoptDeviceRequest request) {
        String path = String.format(ADOPT_PATH_TEMPLATE, siteId);
        return client.postAsync(path, request);
    }

    /**
     * Performs an action on a specific device port.
     *
     * @param siteId   the site ID
     * @param deviceId the device ID
     * @param portIdx  the port index
     * @param action   the port action (e.g. {@link PortAction#POWER_CYCLE})
     * @return the raw response body
     * @see <a href="https://developer.ui.com/network/v10.1.84/executeportaction">Execute Port Action</a>
     */
    public UnifiAction<String> executePortAction(
            @NotNull String siteId,
            @NotNull String deviceId,
            @NotNull String portIdx,
            @NotNull PortAction action) {
        String path = String.format(PORT_ACTION_PATH_TEMPLATE, siteId, deviceId, portIdx);
        return client.postAsync(path, Map.of("action", action.getApiValue()));
    }

    /**
     * Executes an action on an adopted device.
     *
     * @param siteId   the site ID
     * @param deviceId the adopted device ID
     * @param action   the device action (e.g. {@link AdoptedDeviceAction#RESTART})
     * @return the raw response body
     * @see <a href="https://developer.ui.com/network/v10.1.84/executeadopteddeviceaction">Execute Adopted Device Action</a>
     */
    public UnifiAction<String> executeAdoptedDeviceAction(
            @NotNull String siteId,
            @NotNull String deviceId,
            @NotNull AdoptedDeviceAction action) {
        String path = String.format(ADOPTED_DEVICE_ACTION_PATH_TEMPLATE, siteId, deviceId);
        return client.postAsync(path, Map.of("action", action.getApiValue()));
    }
}
