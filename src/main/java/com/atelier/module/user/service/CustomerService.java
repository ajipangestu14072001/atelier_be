package com.atelier.module.user.service;

import com.atelier.module.auth.model.entity.TUserAuth;
import com.atelier.module.auth.model.response.PermissionResponse;
import com.atelier.module.auth.model.response.RoleResponse;
import com.atelier.module.auth.repository.TUserAuthRepository;
import com.atelier.module.user.model.entity.MRole;
import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.user.model.request.DetailUserRequest;
import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.CustomerManagementResponse;
import com.atelier.module.user.model.response.InactiveDTO;
import com.atelier.module.user.model.response.UserDetailResponse;
import com.atelier.module.user.repository.MUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private MUserRepository userRepository;

    @Autowired
    private TUserAuthRepository userAuthRepository;

    public List<CustomerManagementResponse> getCustomers(int page, int size, String sortField, String order) {
        Pageable pageable = createPageable(page, size, sortField, order);
        Page<MUser> userPage = userRepository.findAll(pageable);
        return mapUsersToResponse(userPage);
    }

    public List<CustomerManagementResponse> searchCustomers(String keyword, int page, int size, String sortField, String order) {
        Pageable pageable = createPageable(page, size, sortField, order);

        Page<MUser> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return mapUsersToResponse(userPage);
    }

    public int getTotalCustomers() {
        return (int) userRepository.count();
    }

    public CountDTO getCountInfo() {
        CountDTO countDTO = new CountDTO();
        countDTO.setTotalCustomer(getTotalCustomers());
        countDTO.setTotalMembership(100);
        countDTO.setTotalNonMembership(25);
        return countDTO;
    }

    public InactiveDTO getInactiveInfo() {
        InactiveDTO inactiveDTO = new InactiveDTO();
        inactiveDTO.setTotalCustomer(5);
        inactiveDTO.setTotalMembership(5);
        inactiveDTO.setTotalNonMembership(5);
        return inactiveDTO;
    }

    private Pageable createPageable(int page, int size, String sortField, String order) {
        Sort sort = Sort.by(sortField);
        if ("desc".equalsIgnoreCase(order)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return PageRequest.of(page, size, sort);
    }

    private List<CustomerManagementResponse> mapUsersToResponse(Page<MUser> userPage) {
        return userPage.getContent()
                .stream()
                .map(user -> new CustomerManagementResponse(
                        user.getPublicId() != null ? user.getPublicId().toString() : "",
                        user.getUsername() != null ? user.getUsername() : "",
                        user.getEmail() != null ? user.getEmail() : "",
                        user.getPhone() != null ? user.getPhone() : "",
                        "Gold",
                        user.getStatus() != null && user.getStatus() ? "Active" : "Inactive",
                        user.getUpdateDate() != null ? user.getUpdateDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public UserDetailResponse getUserDetails(DetailUserRequest detailUserRequest) {
        Optional<MUser> optionalUser = userRepository.findByPublicId(UUID.fromString(detailUserRequest.getUserID()));

        if (optionalUser.isPresent()) {
            MUser user = optionalUser.get();
            UserDetailResponse userDetailResponse = new UserDetailResponse();

            userDetailResponse.setUsername(user.getUsername() != null ? user.getUsername() : "");
            userDetailResponse.setEmail(user.getEmail() != null ? user.getEmail() : "");
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

            userDetailResponse.setProfilePicture(user.getProfilePicture() != null ? user.getProfilePicture() : "");
            userDetailResponse.setAddress(new ArrayList<>());
            userDetailResponse.setTransactionHistory(new ArrayList<>());

            return userDetailResponse;
        } else {
            return null;
        }
    }
}
