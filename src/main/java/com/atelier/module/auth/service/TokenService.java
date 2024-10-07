package com.atelier.module.auth.service;

import com.atelier.module.user.model.entity.MUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service("authTokenService")
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;


    public String generateAccessToken(MUser user) {
        return generateToken(user, 3600, "email", user.getEmail(), "username", user.getUsername());
    }

    public String generateRefreshToken(MUser user) {
        return generateToken(user, 7 * 24 * 3600, null, null, null, null);
    }

    private String generateToken(MUser user, long expirationTimeInSeconds, String claimKey, Object claimValue, String usernameKey, String usernameValue) {
        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expirationTimeInSeconds, ChronoUnit.SECONDS))
                .subject(user.getInternalId());

        if (claimKey != null && claimValue != null) {
            claimsBuilder.claim(claimKey, claimValue);
        }

        if (usernameKey != null && usernameValue != null) {
            claimsBuilder.claim(usernameKey, usernameValue);
        }

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }

    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaimAsString("username");
    }

    public String extractInternalId(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }
}
