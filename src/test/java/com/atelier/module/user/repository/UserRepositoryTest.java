//package com.atelier.module.user.repository;
//
//import com.atelier.module.user.model.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void testSaveAndFindUser() {
//        User user = new User("User1", "user1@example.com", "token1");
//        User savedUser = userRepository.save(user);
//
//        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
//        assertNotNull(foundUser);
//        assertEquals(savedUser.getId(), foundUser.getId());
//        assertEquals("User1", foundUser.getUsername());
//    }
//
//    @Test
//    void testCountUsers() {
//        long initialCount = userRepository.count();
//        User user = new User("User2", "user2@example.com", "token2");
//        userRepository.save(user);
//
//        long finalCount = userRepository.count();
//        assertEquals(initialCount + 1, finalCount);
//    }
//}
