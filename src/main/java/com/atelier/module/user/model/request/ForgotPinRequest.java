package com.atelier.module.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPinRequest {
    @NotBlank
    @Email
    private String email;
}