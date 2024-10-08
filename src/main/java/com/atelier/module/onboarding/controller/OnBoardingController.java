package com.atelier.module.onboarding.controller;

import com.atelier.common.util.ApiResponse;
import com.atelier.common.util.ResponseUtils;
import com.atelier.module.onboarding.model.OnboardingItem;
import com.atelier.module.onboarding.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/onboarding")
public class OnBoardingController {

    @Autowired
    private OnboardingService onboardingService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getOnboardingData() {
        List<OnboardingItem> onboardingItems = onboardingService.getOnboardingData();
        return ResponseUtils.createResponse(
                onboardingItems,
                "Onboarding retrieved successfully",
                HttpStatus.OK
        );
    }
}