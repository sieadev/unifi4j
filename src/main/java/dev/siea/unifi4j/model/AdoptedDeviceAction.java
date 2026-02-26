package dev.siea.unifi4j.model;

/**
 * Supported actions for execute adopted device action API.
 *
 * @see <a href="https://developer.ui.com/network/v10.1.84/executeadopteddeviceaction">Execute Adopted Device Action</a>
 */
public enum AdoptedDeviceAction {
    RESTART("restart");

    private final String apiValue;

    AdoptedDeviceAction(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}
