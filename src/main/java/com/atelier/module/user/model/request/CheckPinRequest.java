package com.atelier.module.user.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckPinRequest {
    @NotBlank(message = "PIN must not be blank")
    private String pin;
}
