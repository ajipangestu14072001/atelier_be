package com.atelier.module.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePinRequest {
    @NotBlank(message = "New PIN must not be empty")
    private String newPin;

    @NotBlank(message = "User ID must not be empty")
    private String userID;
}

