package com.atelier.module.user.service;

import com.atelier.common.util.ResponseUtils;
import com.atelier.module.user.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    // Empty list for demonstration purposes
    private static final List<User> USERS = new ArrayList<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();


    @PostConstruct
    public void initUsers() {
        for (int i = 1; i <= 50; i++) {
            User user = new User();
            user.setId(ID_GENERATOR.incrementAndGet());
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setToken("token" + i);
            USERS.add(user);
        }
    }

    public User findById(Long id) {
        return USERS.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll(int page, int size) {
        return ResponseUtils.paginate(USERS, page, size);
    }

    public int countUsers() {
        return USERS.size();
    }
}
