package com.atelier.module.user.controller;

import com.atelier.module.user.model.User;
import com.atelier.module.user.service.UserService;
import com.atelier.common.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<User> users = userService.findAll(page, size);
        int totalUsers = userService.countUsers();
        HttpStatus status = users.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        String message = users.isEmpty() ? "No users found" : "Users retrieved successfully";

        return ResponseUtils.createPagedResponse(
                users.isEmpty() ? null : users,
                totalUsers,
                (totalUsers + size - 1) / size,
                page,
                size,
                message,
                status
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by its ID")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseUtils.createResponse(
                user,
                user == null ? "User not found" : "User retrieved successfully",
                user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
        );
    }
}
