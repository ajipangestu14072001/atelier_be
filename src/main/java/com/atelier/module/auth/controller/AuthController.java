package com.atelier.module.auth.controller;

import com.atelier.common.util.ApiResponse;
import com.atelier.common.util.ResponseUtils;
import com.atelier.module.auth.model.request.LoginRequest;
import com.atelier.module.auth.model.request.RegisterRequest;
import com.atelier.module.auth.model.response.LoginResponse;
import com.atelier.module.auth.service.AuthService;
import com.atelier.module.user.model.request.ForgotPinRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseUtils.createResponse(null, "User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.loginUser(request);
        return ResponseUtils.createResponse(loginResponse, "Login successful.", HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestHeader("Authorization") String token) {
        String actualToken = token.substring(7);
        authService.logout(actualToken);
        return ResponseUtils.createResponse(null,"Logged out successfully", HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<?>> forgotPin(@RequestBody @Valid ForgotPinRequest request) {
        authService.forgotPin(request);
        return ResponseUtils.createResponse(null, "Reset link sent to your email.", HttpStatus.OK);
    }

}


