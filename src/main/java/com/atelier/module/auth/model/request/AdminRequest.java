package com.atelier.module.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    private String adminID;
    private String role;
    private String fullName;
    private String emailAddress;
}

