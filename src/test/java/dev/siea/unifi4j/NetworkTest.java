package dev.siea.unifi4j;

import dev.siea.unifi4j.model.device.DevicesResponse;
import dev.siea.unifi4j.model.site.Site;
import dev.siea.unifi4j.model.site.SitesResponse;
import dev.siea.unifi4j.service.DeviceService;
import dev.siea.unifi4j.service.NetworkService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("dev.siea.unifi4j.TestConfig#isConfigured")
public class NetworkTest {
    private static final Logger logger = LogManager.getLogger("Test");
    private static Unifi4J unifi4J;

    @BeforeAll
    public static void setup() {
        unifi4J = Unifi4J.withApiKey(TestConfig.apiKey())
                .withBaseUri(TestConfig.baseUri())
                .allowInsecureSsl(TestConfig.allowInsecureSsl())
                .build();
    }

    @Test
    public void retrieveSitesAndDevices() {
        NetworkService network = unifi4J.getService(NetworkService.class);
        DeviceService device = unifi4J.getService(DeviceService.class);

        SitesResponse sitesResponse = network.getSites().complete();
        int siteCount = sitesResponse.getTotalCount();
        logger.info("Site count: " + siteCount);

        int totalDeviceCount = 0;
        if (sitesResponse.getData() != null) {
            for (Site site : sitesResponse.getData()) {
                DevicesResponse devicesResponse = device.getDevices(site.getId().toString()).complete();
                int deviceCount = devicesResponse.getTotalCount();
                totalDeviceCount += deviceCount;
                logger.info("Site '" + (site.getName() != null ? site.getName() : site.getId()) + "' (" + site.getId() + "): " + deviceCount + " devices");
            }
        }
        logger.info("Total device count: " + totalDeviceCount);
    }
}
