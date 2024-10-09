package com.atelier.module.user.model.request;

import lombok.Data;

@Data
public class AdminActionRequest {
    private String adminID;
    private String reason;
    private String action;
}
