package com.atelier.module.user.model.response;

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
    private List<String> role;
    private String profilePicture;
    private boolean activeBiometric;
    private List<Object> address;
    private List<Object> transactionHistory;
}
