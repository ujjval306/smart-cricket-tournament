package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.UserListRequest;
import com.example.smart_cricket_tournament.dto.UserListResponse;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.service.UserService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/allUsers")
    public ResponseEntity<ApiResponse<UserListResponse<User>>> getAllUsers(@RequestBody UserListRequest userListRequest) {
     return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "All Users fetched successfully",userService.getAllUsers(userListRequest)));
    }
}
