package com.atelier.module.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerManagementResponse {
    private String userID;
    private String username;
    private String email;
    private String phoneNumber;
    private String customerType;
    private String status;
    private String updateAt;
}
