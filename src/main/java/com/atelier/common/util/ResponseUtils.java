package com.atelier.common.util;

import com.atelier.module.user.model.response.CountDTO;
import com.atelier.module.user.model.response.InactiveDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResponseUtils {

    public static <T> ResponseEntity<ApiResponse<?>> createResponse(T data, String message, HttpStatus status) {
        if (data instanceof List<?>) {
            return createListResponse((List<?>) data, message, status);
        } else {
            return createObjectResponse(data, message, status);
        }
    }

    private static <T> ResponseEntity<ApiResponse<?>> createObjectResponse(T data, String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(
                status.value(),
                message,
                data != null ? data : createEmptyInstance()
        );
        return new ResponseEntity<>(response, status);
    }

    private static <T> ResponseEntity<ApiResponse<?>> createListResponse(List<T> data, String message, HttpStatus status) {
        ApiResponse<List<T>> response = new ApiResponse<>(
                status.value(),
                message,
                data != null ? (data.isEmpty() ? Collections.emptyList() : data) : Collections.emptyList()
        );
        return new ResponseEntity<>(response, status);
    }

    public static <T> List<T> getDefaultListIfNull(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }

    public static <T> List<T> paginate(List<T> data, int page, int size) {
        int start = Math.min(data.size(), page * size);
        int end = Math.min(start + size, data.size());
        return data.subList(start, end);
    }

    public static <T> ResponseEntity<PagedResponse<T>> createPagedResponse(
            List<T> data,
            CountDTO countInfo,
            InactiveDTO inactiveInfo,
            int totalElements,
            int totalPages,
            int currentPage,
            int pageSize,
            String message,
            HttpStatus status
    ) {
        PagedResponse<T> response = new PagedResponse<>(
                status.value(),
                message,
                getDefaultListIfNull(data),
                countInfo,
                inactiveInfo,
                totalElements,
                totalPages,
                currentPage,
                pageSize
        );
        return new ResponseEntity<>(response, status);
    }

    public static ApiResponse<Object> createCustomResponse(int status, String message) {
        return new ApiResponse<>(
                status,
                message,
                new Object()
        );
    }

    public static void handleUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        ResponseEntity<ApiResponse<?>> customResponse = createResponse(null, message, HttpStatus.UNAUTHORIZED);
        String jsonResponse = convertApiResponseToJson(Objects.requireNonNull(customResponse.getBody()));
        response.setStatus(customResponse.getStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    public static String convertApiResponseToJson(ApiResponse<?> apiResponse) {
        String dataJson = "{}";
        return "{"
                + "\"responseCode\": " + apiResponse.getResponseCode() + ", "
                + "\"responseTime\": \"" + Instant.now().toString() + "\", "
                + "\"message\": \"" + apiResponse.getMessage() + "\", "
                + "\"data\": " + dataJson
                + "}";
    }

    @SuppressWarnings("unchecked")
    private static <T> T createEmptyInstance() {
        try {
            return (T) Map.of();
        } catch (Exception e) {
            return null;
        }
    }
}
