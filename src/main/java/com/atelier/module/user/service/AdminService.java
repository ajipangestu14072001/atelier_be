package com.atelier.module.user.service;

import com.atelier.module.auth.model.entity.TUserAuth;
import com.atelier.module.auth.model.entity.TUserSession;
import com.atelier.module.auth.model.request.AdminRequest;
import com.atelier.module.auth.model.response.PermissionResponse;
import com.atelier.module.auth.model.response.RoleResponse;
import com.atelier.module.auth.repository.TUserAuthRepository;
import com.atelier.module.auth.repository.TUserSessionRepository;
import com.atelier.module.user.model.AccountStatus;
import com.atelier.module.user.model.entity.MRole;
import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.user.model.request.AdminActionRequest;
import com.atelier.module.user.model.request.DetailAdminRequest;
import com.atelier.module.user.model.response.AdminDetailResponse;
import com.atelier.module.user.model.response.AdminManagementResponse;
import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.InactiveDTO;
import com.atelier.module.user.repository.MRoleRepository;
import com.atelier.module.user.repository.MUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final MUserRepository mUserRepository;
    private final MRoleRepository mRoleRepository;
    private final TUserAuthRepository tUserAuthRepository;
    private final TUserSessionRepository tUserSessionRepository;

    @Autowired
    public AdminService(MUserRepository mUserRepository, MRoleRepository mRoleRepository, TUserAuthRepository tUserAuthRepository, TUserSessionRepository tUserSessionRepository) {
        this.mUserRepository = mUserRepository;
        this.mRoleRepository = mRoleRepository;
        this.tUserAuthRepository = tUserAuthRepository;
        this.tUserSessionRepository = tUserSessionRepository;
    }

    @Transactional
    public void addAdmin(AdminRequest adminRequest) {
        MRole role = mRoleRepository.findByRole(adminRequest.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        String[] nameParts = adminRequest.getFullName().split("\\s+");
        String firstName = nameParts[0];
        String lastName = "";
        if (nameParts.length > 1) {
            lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));
        }

        MUser admin = new MUser();
        admin.setInternalId(adminRequest.getAdminID());
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(adminRequest.getEmailAddress());
        admin.setRole(role);
        admin.setCreatedDate(LocalDateTime.now());
        admin.setCreatedBy("system");
        admin.setStatus(true);
        mUserRepository.save(admin);
        TUserAuth adminAuth = new TUserAuth();
        adminAuth.setUser(admin);
        adminAuth.setPassword("123456");
        adminAuth.setInternalId(UUID.randomUUID().toString());
        adminAuth.setTermCondition(true);
        adminAuth.setCreatedDate(LocalDateTime.now());

        tUserAuthRepository.save(adminAuth);

        createUserSession(admin);
    }

    private void createUserSession(MUser user) {
        TUserSession newUserSession = new TUserSession();
        newUserSession.setUser(user);
        newUserSession.setSessionId(UUID.randomUUID().toString());
        newUserSession.setIsActive(true);
        newUserSession.setLastActivity(LocalDateTime.now());
        newUserSession.setInternalId(UUID.randomUUID().toString());
        newUserSession.setCreatedDate(LocalDateTime.now());
        tUserSessionRepository.save(newUserSession);
    }

    public List<AdminManagementResponse> getAdmins(int page, int size, String field, String order) {
        Pageable pageable;

        Sort sort = Sort.by(field);
        if ("desc".equalsIgnoreCase(order)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        pageable = PageRequest.of(page, size, sort);
        Page<MUser> userPage = mUserRepository.findAll(pageable);

        return userPage.getContent()
                .stream()
                .map(user -> new AdminManagementResponse(
                        user.getInternalId(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getRole().getInternalId(),
                        user.getRole().getRole(),
                        user.getStatus() != null && user.getStatus() ? "Active" : "Inactive",
                        user.getUpdateDate() != null ? user.getUpdateDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public AdminDetailResponse getAdminDetails(DetailAdminRequest detailAdminRequest) {
        Optional<MUser> optionalUser = mUserRepository.findByPublicId(UUID.fromString(detailAdminRequest.getAdminID()));

        if (optionalUser.isPresent()) {
            MUser admin = optionalUser.get();
            AdminDetailResponse adminDetailResponse = new AdminDetailResponse();

            adminDetailResponse.setAdminID(admin.getPublicId().toString());
            adminDetailResponse.setAdminName(admin.getFirstName() + " " + admin.getLastName());
            adminDetailResponse.setEmail(admin.getEmail());
            adminDetailResponse.setStatus(admin.getStatus() != null && admin.getStatus() ? "Active" : "Inactive");

            MRole role = admin.getRole();
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
            adminDetailResponse.setRole(roleResponse);

            return adminDetailResponse;
        } else {
            return null;
        }
    }

    public void performAdminAction(AdminActionRequest adminActionRequest) {
        UUID publicId = UUID.fromString(adminActionRequest.getAdminID());

        MUser user = mUserRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        user.setNote(adminActionRequest.getReason());

        switch (adminActionRequest.getAction().toUpperCase()) {
            case "TERMINATE":
                user.setAccountStatus(AccountStatus.TERMINATE);
                user.setStatus(false);
                user.setNote(adminActionRequest.getReason());
                break;
            case "DEACTIVATE":
                user.setAccountStatus(AccountStatus.DEACTIVATED);
                user.setStatus(false);
                user.setNote(adminActionRequest.getReason());
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + adminActionRequest.getAction());
        }

        mUserRepository.save(user);
    }

    public int getTotalAdmins() {
        return (int) mUserRepository.count();
    }

    public CountDTO getCountInfo() {
        CountDTO countDTO = new CountDTO();
        countDTO.setTotalCustomer(getTotalAdmins());
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
}


