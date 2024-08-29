//package com.atelier.module.user.service;
//
//import com.atelier.module.user.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserServiceTest {
//
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userService = new UserService();
//    }
//
//    @Test
//    void testFindById() {
//        User user = userService.findById(1L);
//        assertNotNull(user);
//        assertEquals(1L, user.getId());
//        assertEquals("User1", user.getUsername());
//    }
//
//    @Test
//    void testFindAll() {
//        List<User> users = userService.findAll(0, 10);
//        assertNotNull(users);
//        assertEquals(10, users.size());
//    }
//
//    @Test
//    void testCountUsers() {
//        int count = userService.countUsers();
//        assertEquals(100, count);
//    }
//}
