package com.atelier.common.util;

import java.util.List;

public class Constant {
    public static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v1/auth/**"
    );
}
