package com.atelier.common.config;

import com.atelier.common.util.ResponseUtils;
import com.atelier.module.auth.service.AuthService;
import com.atelier.module.auth.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    public JwtTokenValidationFilter(TokenService tokenService, UserDetailsService userDetailsService, AuthService authService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);

            if (authService.isTokenBlacklisted(token)) {
                ResponseUtils.handleUnauthorizedResponse(response, "Token is blacklisted");
                return;
            }

            try {
                // Extract username from token
                String username = tokenService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (JwtException e) {
                ResponseUtils.handleUnauthorizedResponse(response, "Unauthorized: Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}


