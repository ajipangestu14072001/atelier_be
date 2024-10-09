package com.atelier.module.user.model.response;

import com.atelier.module.auth.model.response.RoleResponse;
import lombok.Data;

@Data
public class AdminDetailResponse {
    private String adminID;
    private String status;
    private String adminName;
    private RoleResponse role;
    private String email;
}
