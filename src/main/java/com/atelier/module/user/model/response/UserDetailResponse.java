package com.atelier.module.user.model.response;

import com.atelier.module.auth.model.response.RoleResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailResponse {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String idCardNumber;
    private String birthDate;
    private Object membership;
    private RoleResponse role;
    private String profilePicture;
    private boolean activeBiometric;
    private List<Object> address;
    private List<Object> transactionHistory;
}
