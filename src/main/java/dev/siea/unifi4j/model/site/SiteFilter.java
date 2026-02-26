package dev.siea.unifi4j.model.site;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A single filter condition for the sites endpoint.
 * For {@link SiteFilterOperator#IN} and {@link SiteFilterOperator#NOT_IN}, pass comma-separated values
 * or use {@link #of(SiteFilterField, SiteFilterOperator, String...)}.
 */
public class SiteFilter {

    private final SiteFilterField field;
    private final SiteFilterOperator operator;
    private final String value;

    public SiteFilter(@NotNull SiteFilterField field, @NotNull SiteFilterOperator operator, @NotNull String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public static SiteFilter of(SiteFilterField field, SiteFilterOperator operator, String value) {
        return new SiteFilter(field, operator, value);
    }

    /** For IN / NOT_IN, multiple values are typically comma-separated in the API. */
    public static SiteFilter of(SiteFilterField field, SiteFilterOperator operator, String... values) {
        String value = String.join(",", values);
        return new SiteFilter(field, operator, value);
    }

    public static SiteFilter of(SiteFilterField field, SiteFilterOperator operator, List<String> values) {
        String value = values == null ? "" : String.join(",", values);
        return new SiteFilter(field, operator, value);
    }

    /** Encodes this filter as {@code field:operator:value} for the filter query parameter. */
    public String toQueryValue() {
        return field.getQueryName() + ":" + operator.getQueryName() + ":" + value;
    }

    public SiteFilterField getField() {
        return field;
    }

    public SiteFilterOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}
