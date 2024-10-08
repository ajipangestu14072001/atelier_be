package com.atelier.module.user.model.response;

import lombok.Data;

@Data
public class InactiveDTO {
    private int totalCustomer;
    private int totalMembership;
    private int totalNonMembership;
}