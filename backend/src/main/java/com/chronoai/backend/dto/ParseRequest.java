package com.chronoai.backend.dto;

import lombok.Data;

@Data
public class ParseRequest {
    private String query;

    // Manually added getter for query
    public String getQuery() {
        return query;
    }
}