package com.example.smart_cricket_tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse<T> {
    private List<T> data;
    private long totalRecords;
}
