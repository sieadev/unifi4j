package dev.siea.unifi4j.model.device;

import org.jetbrains.annotations.NotNull;

/**
 * Request body for adopt device API.
 *
 * @see <a href="https://developer.ui.com/network/v10.1.84/adoptdevice">Adopt Device</a>
 */
public class AdoptDeviceRequest {

    private final String macAddress;
    private final boolean ignoreDeviceLimit;

    public AdoptDeviceRequest(@NotNull String macAddress, boolean ignoreDeviceLimit) {
        this.macAddress = macAddress;
        this.ignoreDeviceLimit = ignoreDeviceLimit;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isIgnoreDeviceLimit() {
        return ignoreDeviceLimit;
    }
}
