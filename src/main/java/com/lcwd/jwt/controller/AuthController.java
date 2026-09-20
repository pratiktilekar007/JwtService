package com.lcwd.jwt.controller;

import com.lcwd.jwt.dto.AuthRequest;
import com.lcwd.jwt.dto.AuthResponse;
import com.lcwd.jwt.dto.TokenValidationResponse;
import com.lcwd.jwt.entity.User;
import com.lcwd.jwt.service.AuthService;
import com.lcwd.jwt.service.JwtService;
import com.lcwd.jwt.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService ;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, UserDetailsServiceImpl userDetailsService, JwtService jwtService) {
        this.authService=authService;
        this.userDetailsService=userDetailsService;
        this.jwtService=jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    /**
     * Called only by API Gateway.  JwtService remains the authority that checks
     * the signature, expiry, and that the user identified by the token exists.
     */
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        System.out.println("authorization : " + authorization );
        if (authorization == null || !authorization.startsWith("Bearer ") || authorization.length() <= 7) {
            return ResponseEntity.status(401).build();
        }

        try {
            String token = authorization.substring(7);
            String username = jwtService.extractUsername(token);
            var user = userDetailsService.loadUserByUsername(username);
            if (!jwtService.isTokenValid(token, user)) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok(new TokenValidationResponse(true, username));
        } catch (JwtException | IllegalArgumentException exception) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/current-user")
    public User getcurrentUser(Principal principal)
    {
        return ((User)this.userDetailsService.loadUserByUsername(principal.getName()));
    }
}
