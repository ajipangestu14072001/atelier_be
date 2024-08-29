package com.atelier.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PagedResponse<T> {
    private int responseCode;
    private String responseTime;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> data;

    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PagedResponse(int responseCode, String message, List<T> data, int totalElements, int totalPages, int currentPage, int pageSize) {
        this.responseCode = responseCode;
        this.responseTime = Instant.now().toString();
        this.message = message;
        this.data = data;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
