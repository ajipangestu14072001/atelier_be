package com.atelier.module.auth.service;

import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class TokenStore {

    private static final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();
    private static final long TOKEN_EXPIRY_TIME = TimeUnit.HOURS.toMillis(1);

    public static void storeTokenForUser(String userId, String token) {
        tokenStore.put(token, userId);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tokenStore.remove(token);
            }
        }, TOKEN_EXPIRY_TIME);
    }

    public static boolean validateToken(String token) {
        return tokenStore.containsKey(token);
    }

    public static String getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }
}

