package com.example.smart_cricket_tournament.dto;

import lombok.Data;

@Data
public class UserListRequest {
    private String search = "";
    private int start = 0;
    private int limit = 10;
    private String sort = "id";
    private String order = "asc";
    private String filter;
}
