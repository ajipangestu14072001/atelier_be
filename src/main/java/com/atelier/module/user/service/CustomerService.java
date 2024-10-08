package com.atelier.module.user.service;

import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.CustomerManagementResponse;
import com.atelier.module.user.model.response.InactiveDTO;
import com.atelier.module.user.repository.MUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private MUserRepository userRepository;

    public List<CustomerManagementResponse> getCustomers(int page, int size, String sortField, String order) {
        Pageable pageable;

        Sort sort = Sort.by(sortField);
        if ("desc".equalsIgnoreCase(order)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        pageable = PageRequest.of(page, size, sort);
        Page<MUser> userPage = userRepository.findAll(pageable);

        return userPage.getContent()
                .stream()
                .map(user -> new CustomerManagementResponse(
                        user.getPublicId().toString(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        "Gold",
                        user.getStatus() != null && user.getStatus() ? "Active" : "Inactive",
                        user.getUpdateDate() != null ? user.getUpdateDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public List<CustomerManagementResponse> searchCustomers(String keyword, int page, int size, String sortField, String order) {
        Pageable pageable;

        Sort sort = Sort.by(sortField);
        if ("desc".equalsIgnoreCase(order)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        pageable = PageRequest.of(page, size, sort);

        Page<MUser> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.getContent()
                .stream()
                .map(user -> new CustomerManagementResponse(
                        user.getPublicId().toString(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        "Gold",
                        user.getStatus() != null && user.getStatus() ? "Active" : "Inactive",
                        user.getUpdateDate() != null ? user.getUpdateDate().toString() : ""
                ))
                .collect(Collectors.toList());
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
}
