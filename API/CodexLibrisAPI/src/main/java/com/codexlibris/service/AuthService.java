/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.service;

import com.codexlibris.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.codexlibris.model.User;
import com.codexlibris.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author jessica
 */
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    
    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,JwtService jwtService, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public AuthResponse authenticate(String username, String password) {
        System.out.println("🟢 4 - Auth user: " + username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        System.out.println("🟢 4.1 - AuthenticationManager passed");

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.out.println("🔴 4.2 - Usuari no trobat");
                return new RuntimeException("Usuari no trobat");
            });

        System.out.println("🟢 5 - In DB: " + user.getPassword());
        System.out.println("🟢 6 - In POST: " + password);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("🔴 7 - Incorrect password");
            throw new BadCredentialsException("Usuari o contrasenya incorrecte");
        }

        String token = jwtService.generateToken(user, String.valueOf(user.getRole().getId()));

        System.out.println("🟢 8 - Token generat: " + token);
  
        return new AuthResponse(token, user.getUsername(), user.getRole().getId());
    }


}
