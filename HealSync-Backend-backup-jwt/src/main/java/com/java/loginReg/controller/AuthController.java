package com.java.loginReg.controller;

import com.java.loginReg.entities.User;
import com.java.loginReg.entities.*;
import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.jwt.JwtUtils;
import com.java.loginReg.payload.AuthRequest;
import com.java.loginReg.payload.AuthResponse;
import com.java.loginReg.payload.RegisterRequest;
import com.java.loginReg.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtUtils.generateJwtToken(auth.getName());
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // 1) Check for existing email
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: Email is already in use!");
        }

        // 2) Build new User
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.valueOf(req.getRole().toUpperCase()));
        user.setGender(Gender.valueOf(req.getGender().toUpperCase()));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());

        // 3) Save to DB
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }
}
