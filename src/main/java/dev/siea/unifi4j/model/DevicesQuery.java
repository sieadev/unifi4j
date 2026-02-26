package dev.siea.unifi4j.model;

import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Query parameters for the devices endpoint: pagination (offset, limit) and optional filter string.
 */
public class DevicesQuery {

    private final Integer offset;
    private final Integer limit;
    private final String filter;

    private DevicesQuery(Integer offset, Integer limit, String filter) {
        this.offset = offset;
        this.limit = limit;
        this.filter = filter;
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
        if (filter != null && !filter.isBlank()) {
            params.add("filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8));
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

    @Nullable
    public String getFilter() {
        return filter;
    }

    public static final class Builder {
        private Integer offset;
        private Integer limit;
        private String filter;

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder filter(@Nullable String filter) {
            this.filter = filter;
            return this;
        }

        public DevicesQuery build() {
            return new DevicesQuery(offset, limit, filter);
        }
    }
}
