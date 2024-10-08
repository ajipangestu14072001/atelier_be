package com.atelier.module.onboarding.service;

import com.atelier.module.onboarding.model.OnboardingItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnboardingService {

    public List<OnboardingItem> getOnboardingData() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();
        onboardingItems.add(createOnboardingItem("14c1fcd7-b3bb-46c1-8905-bfc56cc76686", "Find Your Desired Events and Merchandise"));
        onboardingItems.add(createOnboardingItem("24c1fcd7-b3bb-46c1-8905-bfc56cc76686", "Find Your Desired Events and Merchandise"));
        onboardingItems.add(createOnboardingItem("34c1fcd7-b3bb-46c1-8905-bfc56cc76686", "Find Your Desired Events and Merchandise"));

        return onboardingItems;
    }

    private OnboardingItem createOnboardingItem(String id, String description) {
        OnboardingItem item = new OnboardingItem();
        item.setId(id);
        item.setIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Google_Chrome_icon_%28September_2014%29.svg/1200px-Google_Chrome_icon_%28September_2014%29.svg.png");
        item.setBanner("https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjC97Z8BResg5dlPqczsRCFhP6zewWX0X0e7fVPG-G7PuUZwwZVsi9OPoqJYkgqT2h0FI95SsmWzVEgpt8b8HAqFiIxZ98TFtY4lE0b8UrtVJ2HrJebRwl6C9DslsQDl9KnBIrdHS6LtkY/s1600/jetpack+compose+icon_RGB.png");
        item.setDescription(description);
        return item;
    }
}
