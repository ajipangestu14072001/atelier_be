package com.atelier.module.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminManagementResponse {
    private String adminID;
    private String adminName;
    private String roleID;
    private String roleName;
    private String status;
    private String updateAt;
}
