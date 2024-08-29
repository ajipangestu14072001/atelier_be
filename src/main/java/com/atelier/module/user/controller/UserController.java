package com.atelier.module.user.controller;

import com.atelier.common.util.ApiResponse;
import com.atelier.module.user.model.NotificationRequest;
import com.atelier.module.user.model.NotificationResponse;
import com.atelier.module.user.model.User;
import com.atelier.module.user.service.UserService;
import com.atelier.common.util.ResponseUtils;
import com.atelier.common.util.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;
    private final TranslationService translationService;

    public UserController(UserService userService, TranslationService translationService) {
        this.userService = userService;
        this.translationService = translationService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language) {

        Locale locale = Locale.forLanguageTag(language);
        List<User> users = userService.findAll(page, size);
        int totalUsers = userService.countUsers();
        HttpStatus status = users.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;

        String message = users.isEmpty()
                ? translationService.getMessage("users.not_found", locale)
                : translationService.getMessage("users.retrieved_successfully", locale);

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
    public ResponseEntity<?> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language) {

        Locale locale = Locale.forLanguageTag(language);
        User user = userService.findById(id);

        String message = user == null
                ? translationService.getMessage("user.not_found", locale)
                : translationService.getMessage("user.retrieved_successfully", locale);

        return ResponseUtils.createResponse(
                user,
                message,
                user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
        );
    }

    @PostMapping("/notification")
    @Operation(summary = "Send a notification", description = "Send a notification to the specified device token")
    public ResponseEntity<ApiResponse<?>> sendNotification(@RequestBody NotificationRequest request) {
        userService.sendNotification(request);
        String message = translationService.getMessage("notification.sent_successfully", Locale.forLanguageTag("en"));
        return ResponseUtils.createResponse(
                null,
                message,
                HttpStatus.OK
        );
    }

}
