package com.atelier.module.user.controller;

import com.atelier.common.util.PagedResponse;
import com.atelier.common.util.ResponseUtils;
import com.atelier.module.user.model.request.SearchRequest;
import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.CustomerManagementResponse;
import com.atelier.module.user.model.response.InactiveDTO;
import com.atelier.module.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/management")
public class CustomerManagementController {
    @Autowired
    private CustomerService customerManagementService;

    @GetMapping("/customer")
    public ResponseEntity<PagedResponse<CustomerManagementResponse>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "username") String sortField,
            @RequestParam(defaultValue = "asc") String order) {

        List<CustomerManagementResponse> allCustomers = customerManagementService.getCustomers(page, pageSize, sortField, order);

        CountDTO countInfo = customerManagementService.getCountInfo();
        InactiveDTO inactiveInfo = customerManagementService.getInactiveInfo();

        return ResponseUtils.createPagedResponse(
                allCustomers,
                countInfo,
                inactiveInfo,
                customerManagementService.getTotalCustomers(),
                (int) Math.ceil((double) customerManagementService.getTotalCustomers() / pageSize),
                page,
                pageSize,
                "Users retrieved successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("/search")
    public ResponseEntity<PagedResponse<CustomerManagementResponse>> searchCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "username") String sortField,
            @RequestParam(defaultValue = "asc") String order,
            @RequestBody SearchRequest searchRequest) {

        List<CustomerManagementResponse> customers = customerManagementService.searchCustomers(
                searchRequest.getKeyword(), page, pageSize, sortField, order
        );

        return ResponseUtils.createPagedResponse(
                customers,
                null,
                null,
                customerManagementService.getTotalCustomers(),
                (int) Math.ceil((double) customerManagementService.getTotalCustomers() / pageSize),
                page,
                pageSize,
                "Search results retrieved successfully",
                HttpStatus.OK
        );
    }

}
