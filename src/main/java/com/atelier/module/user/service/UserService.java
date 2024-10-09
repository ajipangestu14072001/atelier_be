package com.atelier.module.user.service;

import com.atelier.module.auth.model.entity.TUserAuth;
import com.atelier.module.auth.model.response.PermissionResponse;
import com.atelier.module.auth.model.response.RoleResponse;
import com.atelier.module.auth.repository.TUserAuthRepository;
import com.atelier.module.user.model.entity.MRole;
import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.user.model.request.DetailUserRequest;
import com.atelier.module.user.model.request.UpdateUserRequest;
import com.atelier.module.user.model.response.UserDetailResponse;
import com.atelier.module.user.repository.MRoleRepository;
import com.atelier.module.user.repository.MUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private TUserAuthRepository userAuthRepository;

    @Autowired
    private MUserRepository mUserRepository;

    @Autowired
    private MRoleRepository mRoleRepository;

    public boolean checkPin(String pin) {
        Optional<TUserAuth> userAuthOptional = userAuthRepository.findByPassword(pin);
        return userAuthOptional.isPresent();
    }

    public boolean isUserExists(String input) {
        return mUserRepository.findByEmailOrPhone(input, input).isPresent() ||
                mUserRepository.findByUsername(input).isPresent();
    }

    public boolean updateUser(UpdateUserRequest updateUserRequest) {
        Optional<MUser> optionalUser = mUserRepository.findByPublicId(UUID.fromString(updateUserRequest.getUserID()));

        if (optionalUser.isPresent()) {
            MUser user = optionalUser.get();
            boolean isUpdated = false;

            if (updateUserRequest.getProfilePicture() != null && !updateUserRequest.getProfilePicture().isEmpty()) {
                if (!updateUserRequest.getProfilePicture().equals(user.getProfilePicture())) {
                    user.setProfilePicture(updateUserRequest.getProfilePicture());
                    isUpdated = true;
                }
            }

            if (updateUserRequest.getFullName() != null && !updateUserRequest.getFullName().trim().isEmpty()) {
                String[] nameParts = updateUserRequest.getFullName().trim().split(" ");
                String firstName = nameParts[0];
                String lastName = (nameParts.length > 1) ? String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length)) : "";

                if (!firstName.equals(user.getFirstName()) || !lastName.equals(user.getLastName())) {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    isUpdated = true;
                }
            }

            if (updateUserRequest.getPhoneNumber() != null && !updateUserRequest.getPhoneNumber().isEmpty()) {
                if (!updateUserRequest.getPhoneNumber().equals(user.getPhone())) {
                    user.setPhone(updateUserRequest.getPhoneNumber());
                    isUpdated = true;
                }
            }

            if (updateUserRequest.getEmailAddress() != null && !updateUserRequest.getEmailAddress().isEmpty()) {
                if (!updateUserRequest.getEmailAddress().equals(user.getEmail())) {
                    user.setEmail(updateUserRequest.getEmailAddress());
                    isUpdated = true;
                }
            }

            if (updateUserRequest.getIdCardNumber() != null && !updateUserRequest.getIdCardNumber().isEmpty()) {
                if (!updateUserRequest.getIdCardNumber().equals(user.getIdCardNumber())) {
                    user.setIdCardNumber(updateUserRequest.getIdCardNumber());
                    isUpdated = true;
                }
            }

            if (updateUserRequest.getBirthDate() != null) {
                if (!updateUserRequest.getBirthDate().equals(user.getBirthDate())) {
                    user.setBirthDate(updateUserRequest.getBirthDate());
                    isUpdated = true;
                }
            }

            if (isUpdated) {
                mUserRepository.save(user);
            }

            return isUpdated;
        }
        return false;
    }

    public UserDetailResponse getUserDetails(DetailUserRequest detailUserRequest) {
        Optional<MUser> optionalUser = mUserRepository.findByPublicId(UUID.fromString(detailUserRequest.getUserID()));

        if (optionalUser.isPresent()) {
            MUser user = optionalUser.get();
            UserDetailResponse userDetailResponse = new UserDetailResponse();

            userDetailResponse.setUsername(user.getUsername());
            userDetailResponse.setEmail(user.getEmail());
            userDetailResponse.setFullName(user.getFirstName() + " " + user.getLastName());
            userDetailResponse.setPhoneNumber(user.getPhone() != null ? user.getPhone() : "");
            userDetailResponse.setIdCardNumber(user.getIdCardNumber() != null ? user.getIdCardNumber() : "");

            LocalDate birthDate = user.getBirthDate();
            userDetailResponse.setBirthDate(birthDate != null ? birthDate.toString() : "");

            MRole role = user.getRole();
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setRoleId(role.getPublicId().toString());
            roleResponse.setRoleName(role.getRole());

            List<PermissionResponse> permissionResponses = role.getPermissions()
                    .stream()
                    .map(permission -> new PermissionResponse(
                            permission.getIdentifier() != null ? permission.getIdentifier() : "",
                            permission.getName() != null ? permission.getName() : "",
                            permission.getNotes() != null ? permission.getNotes() : "",
                            permission.getType() != null ? permission.getType() : ""
                    ))
                    .collect(Collectors.toList());

            roleResponse.setPermission(permissionResponses);

            userDetailResponse.setRole(roleResponse);

            Optional<TUserAuth> optionalUserAuth = userAuthRepository.findByUser(user);
            if (optionalUserAuth.isPresent()) {
                TUserAuth userAuth = optionalUserAuth.get();
                userDetailResponse.setActiveBiometric(userAuth.getBiometricTemplate() != null);
            } else {
                userDetailResponse.setActiveBiometric(false);
            }

            userDetailResponse.setProfilePicture(user.getProfilePicture());
            userDetailResponse.setAddress(new ArrayList<>());
            userDetailResponse.setTransactionHistory(new ArrayList<>());

            return userDetailResponse;
        } else {
            return null;
        }
    }

}
