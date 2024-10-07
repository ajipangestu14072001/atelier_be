package com.atelier.common.config;

import com.atelier.module.auth.service.AuthService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class PinAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;

    public PinAuthenticationProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String pin = authentication.getCredentials().toString();

        UserDetails userDetails = authService.loadUserByPin(pin);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid PIN");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, pin, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

