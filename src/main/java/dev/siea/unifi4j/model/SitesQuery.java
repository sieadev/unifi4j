package dev.siea.unifi4j.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Query parameters for the sites endpoint: pagination (offset, limit) and optional filters.
 */
public class SitesQuery {

    private final Integer offset;
    private final Integer limit;
    private final List<SiteFilter> filters;

    private SitesQuery(Integer offset, Integer limit, List<SiteFilter> filters) {
        this.offset = offset;
        this.limit = limit;
        this.filters = filters == null ? Collections.emptyList() : new ArrayList<>(filters);
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Builds the query string (e.g. ?offset=0&limit=10&filter=name:eq:foo). */
    public String toQueryString() {
        List<String> params = new ArrayList<>();
        if (offset != null) {
            params.add("offset=" + offset);
        }
        if (limit != null) {
            params.add("limit=" + limit);
        }
        for (SiteFilter filter : filters) {
            params.add("filter=" + filter.toQueryValue());
        }
        if (params.isEmpty()) {
            return "";
        }
        return "?" + String.join("&", params);
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public List<SiteFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public static final class Builder {
        private Integer offset;
        private Integer limit;
        private final List<SiteFilter> filters = new ArrayList<>();

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder filter(@NotNull SiteFilter filter) {
            this.filters.add(filter);
            return this;
        }

        public Builder filter(@NotNull SiteFilterField field, @NotNull SiteFilterOperator operator, @NotNull String value) {
            return filter(SiteFilter.of(field, operator, value));
        }

        public SitesQuery build() {
            return new SitesQuery(offset, limit, filters);
        }
    }
}
