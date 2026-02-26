package dev.siea.unifi4j.model.device;

/**
 * Supported actions for execute port action API.
 *
 * @see <a href="https://developer.ui.com/network/v10.1.84/executeportaction">Execute Port Action</a>
 */
public enum PortAction {
    POWER_CYCLE("power_cycle");

    private final String apiValue;

    PortAction(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}
