package dev.siea.unifi4j.model.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Device connection/operational state.
 */
public enum DeviceState {
    ONLINE,
    OFFLINE,
    ADOPTING,
    ADOPT_FAILED,
    PENDING,
    PROVISIONING,
    UPGRADING,
    UNKNOWN;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static DeviceState fromValue(String value) {
        if (value == null || value.isBlank()) return UNKNOWN;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
