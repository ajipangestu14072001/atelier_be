package com.atelier.module.auth.model.response;

import lombok.Data;

import java.util.List;

@Data
public class RoleResponse {
    private String roleId;
    private String roleName;
    private List<PermissionResponse> permission;
}
