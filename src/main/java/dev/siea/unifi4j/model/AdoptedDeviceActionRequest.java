package dev.siea.unifi4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Request body for execute adopted device action API.
 * Must include the action name and any applicable input arguments.
 *
 * @see <a href="https://developer.ui.com/network/v10.1.84/executeadopteddeviceaction">Execute Adopted Device Action</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdoptedDeviceActionRequest {

    private final String action;
    private final Map<String, Object> arguments;

    public AdoptedDeviceActionRequest(@NotNull String action, @Nullable Map<String, Object> arguments) {
        this.action = action;
        this.arguments = arguments;
    }

    /** Creates a request with only the action name (no extra arguments). */
    public static AdoptedDeviceActionRequest of(@NotNull String action) {
        return new AdoptedDeviceActionRequest(action, null);
    }

    /** Creates a request with action name and optional arguments. */
    public static AdoptedDeviceActionRequest of(@NotNull String action, @Nullable Map<String, Object> arguments) {
        return new AdoptedDeviceActionRequest(action, arguments);
    }

    public String getAction() {
        return action;
    }

    @Nullable
    public Map<String, Object> getArguments() {
        return arguments;
    }
}
