package com.atelier.module.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionResponse {
    private String identifier;
    private String name;
    private String notes;
    private String type;
}
