package com.lcwd.jwt.service;

import com.lcwd.jwt.dto.AuthRequest;
import com.lcwd.jwt.dto.AuthResponse;
import com.lcwd.jwt.exception.InvalidCredentialsException;
import com.lcwd.jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service                        // ← MUST be present for Spring to create the bean
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService  jwtService;
    private final PasswordEncoder passwordEncoder;

    // ── Manual Constructor (no Lombok) ────────────────────────────────────────
    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.jwtService      = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest request) {

        // 1️⃣ Check user exists in DB
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid credentials: user not found"));

        // 2️⃣ Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials: wrong password");
        }

        // 3️⃣ Generate JWT (30-min expiry set in application.yml)
        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getUsername()
        );
    }
}
