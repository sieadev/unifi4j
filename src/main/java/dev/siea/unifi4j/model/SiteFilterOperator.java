package dev.siea.unifi4j.model;

/**
 * Operators supported in sites filter expressions.
 */
public enum SiteFilterOperator {
    EQ("eq"),
    NE("ne"),
    IN("in"),
    NOT_IN("notIn");

    private final String queryName;

    SiteFilterOperator(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }
}
