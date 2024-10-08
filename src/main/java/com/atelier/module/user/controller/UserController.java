package com.atelier.module.user.controller;

import com.atelier.common.util.ApiResponse;
import com.atelier.common.util.ResponseUtils;
import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.user.model.request.CheckAccountRequest;
import com.atelier.module.user.model.request.CheckPinRequest;
import com.atelier.module.user.model.request.DetailUserRequest;
import com.atelier.module.user.model.request.UpdateUserRequest;
import com.atelier.module.user.model.response.UploadProfileImageResponse;
import com.atelier.module.user.model.response.UserDetailResponse;
import com.atelier.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("${api.version}/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService; // Service to handle the business logic

    @PostMapping("/pinCheck")
    public ResponseEntity<ApiResponse<?>> checkPin(@RequestBody CheckPinRequest checkPinRequest) {
        boolean isValid = userService.checkPin(checkPinRequest.getPin());

        if (isValid) {
            return ResponseUtils.createResponse(null, "PIN is valid", HttpStatus.OK);
        } else {
            return ResponseUtils.createResponse(null, "Invalid PIN", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accountCheck")
    public ResponseEntity<ApiResponse<?>> checkData(@RequestBody CheckAccountRequest checkDataRequest) {
        String input = checkDataRequest.getUsernameOrEmailOrPhone();
        boolean isDataExists = userService.isUserExists(input);

        if (isDataExists) {
            return ResponseUtils.createResponse(null, "Username already exists", HttpStatus.CONFLICT);
        } else {
            return ResponseUtils.createResponse(null, "Username/Email/Phone valid", HttpStatus.OK);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        boolean isUpdated = userService.updateUser(updateUserRequest);

        if (isUpdated) {
            return ResponseUtils.createResponse(null, "User data updated successfully", HttpStatus.OK);
        } else {
            return ResponseUtils.createResponse(null, "No updates necessary; data is already up to date", HttpStatus.OK);
        }
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<?>> getUserDetails(@RequestBody DetailUserRequest detailUserRequest) {
        UserDetailResponse userDetailResponse = userService.getUserDetails(detailUserRequest);

        if (userDetailResponse != null) {
            return ResponseUtils.createResponse(userDetailResponse, "User retrieved successfully", HttpStatus.OK);
        } else {
            return ResponseUtils.createResponse(null, "User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<ApiResponse<?>> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            UploadProfileImageResponse responseData = userService.uploadProfileImage(file);
            return ResponseUtils.createResponse(responseData, "Profile image uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            return ResponseUtils.createResponse(null, "Error uploading profile image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/showProfile")
    public ResponseEntity<ApiResponse<?>> getUserProfile(@RequestBody DetailUserRequest userId) {
        try {
            UploadProfileImageResponse userResponse = userService.getUserById(userId.getUserID());

            if (userResponse != null) {
                return ResponseUtils.createResponse(userResponse, "User profile retrieved successfully", HttpStatus.OK);
            } else {
                return ResponseUtils.createResponse(null, "User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseUtils.createResponse(null, "Error retrieving user profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
