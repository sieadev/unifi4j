package dev.siea.unifi4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Paginated response from the sites endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SitesResponse {

    private int offset;
    private int limit;
    private int count;
    @JsonProperty("totalCount")
    private int totalCount;
    private List<Site> data;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Site> getData() {
        return data;
    }

    public void setData(List<Site> data) {
        this.data = data;
    }
}
