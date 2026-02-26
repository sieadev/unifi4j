package dev.siea.unifi4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response from the network integration info endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkInfo {

    @JsonProperty("applicationVersion")
    private String applicationVersion;

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }
}
