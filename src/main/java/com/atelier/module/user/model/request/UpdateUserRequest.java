package com.atelier.module.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String userID;
    private String profilePicture;
    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    private String idCardNumber;
    private LocalDate birthDate;
}
