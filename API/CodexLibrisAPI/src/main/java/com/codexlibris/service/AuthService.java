/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.service;

import com.codexlibris.model.User;
import com.codexlibris.repository.UserRepository;
import com.codexlibris.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author jessica
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(UserRepository userRepository, 
            PasswordEncoder passwordEncoder, 
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public String authenticate(String userName, String password) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Usuari o contrasenya incorrecte"));
        
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Usuari o contrasenya incorrecte");
        }
        
        return jwtUtil.generateToken(userName);
    }
}
