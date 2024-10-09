package com.atelier.module.user.controller;


import com.atelier.common.util.ApiResponse;
import com.atelier.common.util.PagedResponse;
import com.atelier.common.util.ResponseUtils;
import com.atelier.module.auth.model.request.AdminRequest;
import com.atelier.module.user.model.request.AdminActionRequest;
import com.atelier.module.user.model.request.DetailAdminRequest;
import com.atelier.module.user.model.response.AdminDetailResponse;
import com.atelier.module.user.model.response.AdminManagementResponse;
import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.InactiveDTO;
import com.atelier.module.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/management")
public class AdminManagementController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<?>> addAdmin(@RequestBody AdminRequest adminRequest) {
        adminService.addAdmin(adminRequest);
        return ResponseUtils.createResponse(null, "Admin registered successfully.", HttpStatus.CREATED);

    }

    @GetMapping("/admin")
    public ResponseEntity<PagedResponse<AdminManagementResponse>> getAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "username") String sortField,
            @RequestParam(defaultValue = "asc") String order) {

        List<AdminManagementResponse> allAdmins = adminService.getAdmins(page, pageSize, sortField, order);

        CountDTO countInfo = adminService.getCountInfo();
        InactiveDTO inactiveInfo = adminService.getInactiveInfo();

        return ResponseUtils.createPagedResponse(
                allAdmins,
                countInfo,
                inactiveInfo,
                adminService.getTotalAdmins(),
                (int) Math.ceil((double) adminService.getTotalAdmins() / pageSize),
                page,
                pageSize,
                "Admin retrieved successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("/admin/detail")
    public ResponseEntity<ApiResponse<?>> getAdminDetails(@RequestBody DetailAdminRequest detailAdminRequest) {
        AdminDetailResponse adminDetailResponse = adminService.getAdminDetails(detailAdminRequest);

        if (adminDetailResponse != null) {
            return ResponseUtils.createResponse(adminDetailResponse, "Admin retrieved successfully", HttpStatus.OK);
        } else {
            return ResponseUtils.createResponse(null, "Admin not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/action")
    public ResponseEntity<ApiResponse<?>> handleAdminAction(@RequestBody AdminActionRequest adminActionRequest) {
        try {
            adminService.performAdminAction(adminActionRequest);
            return ResponseUtils.createResponse(null, "Admin " + adminActionRequest.getAction().toLowerCase() + " successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtils.createResponse(null, "Error processing action: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

