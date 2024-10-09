package com.atelier.module.auth.model.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String userID;
    private String username;
    private String email;
    private RoleResponse role;
    private String token;
    private String refreshToken;
    private int expired;
    private String tokenType;
}
