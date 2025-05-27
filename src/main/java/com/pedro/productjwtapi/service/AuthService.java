package com.pedro.productjwtapi.service;

import com.pedro.productjwtapi.auth.JwtResponse;
import com.pedro.productjwtapi.auth.LoginRequest;
import com.pedro.productjwtapi.auth.RegisterRequest;
import com.pedro.productjwtapi.model.Role;
import com.pedro.productjwtapi.model.User;
import com.pedro.productjwtapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse register(RegisterRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("Username {} is already taken", request.getUsername());
            throw new IllegalArgumentException("Username is already taken");
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("Password encoded successfully");
        
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("User registered successfully with role: {}", user.getRole());
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }

    public JwtResponse login(LoginRequest request) {
        log.info("Attempting to login user: {}", request.getUsername());
        
        // First check if user exists
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
        
        log.debug("Found user in database, attempting authentication");
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            log.info("User {} authenticated successfully with role: {}", user.getUsername(), user.getRole());
            String jwt = jwtService.generateToken(user);
            return new JwtResponse(jwt);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {} - Bad credentials", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        } catch (Exception e) {
            log.error("Authentication failed for user: {} - {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }
}
