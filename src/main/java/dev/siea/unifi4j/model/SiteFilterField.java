package dev.siea.unifi4j.model;

/**
 * Fields that can be used in a sites filter.
 */
public enum SiteFilterField {
    ID("id"),
    INTERNAL_REFERENCE("internalReference"),
    NAME("name");

    private final String queryName;

    SiteFilterField(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }
}
