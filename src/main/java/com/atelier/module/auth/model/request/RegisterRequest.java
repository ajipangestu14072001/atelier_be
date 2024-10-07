package com.atelier.module.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String phoneNumber;
    private String userName;
    private String email;
    private String pin;
    private Boolean termCondition;
    private String role;
}

