package com.lcwd.jwt.dto;

/** Minimal internal response consumed by API Gateway. */
public record TokenValidationResponse(boolean valid, String username) {
}
