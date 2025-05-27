package com.pedro.productjwtapi.service;

import com.pedro.productjwtapi.auth.JwtResponse;
import com.pedro.productjwtapi.auth.LoginRequest;
import com.pedro.productjwtapi.auth.RegisterRequest;
import com.pedro.productjwtapi.model.Role;
import com.pedro.productjwtapi.model.User;
import com.pedro.productjwtapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse register(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }

    public JwtResponse login (LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }
}
