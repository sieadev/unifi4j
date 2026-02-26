package dev.siea.unifi4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

/**
 * A device in a Unifi site (e.g. switch, gateway, AP).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    private UUID id;
    private String macAddress;
    private String ipAddress;
    private String name;
    private String model;
    private DeviceState state;
    private boolean supported;
    private String firmwareVersion;
    private boolean firmwareUpdatable;
    private List<String> features;
    private List<String> interfaces;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public boolean isFirmwareUpdatable() {
        return firmwareUpdatable;
    }

    public void setFirmwareUpdatable(boolean firmwareUpdatable) {
        this.firmwareUpdatable = firmwareUpdatable;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }
}
