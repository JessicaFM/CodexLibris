/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.LoginRequest;
import com.codexlibris.dto.ErrorResponse;
import com.codexlibris.service.AuthService;
import com.codexlibris.service.JwtService;
import com.codexlibris.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("🟢 1 - Debug: Username recibido -> " + request.getUsername());
        System.out.println("🟢 Debug: Password recibido -> " + request.getPassword());
    
        logger.info("🟢 1 - Recibida solicitud de login para usuario: {}", request.getUsername());

        try {
            AuthResponse authResponse = authService.authenticate(request.getUsername(), request.getPassword());
            logger.info("🟢 10 - Respuesta enviada con token: {}", authResponse.getToken());

            return ResponseEntity.ok().body(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Usuario o contraseña incorrectos"));
        } catch (Exception e) {
            logger.error("🔴 Error en login: ", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Error interno del servidor"));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            jwtService.blacklistToken(token);
            return ResponseEntity.ok().body("{\"message\": \"Logged out successfully\"}");
        }

        return ResponseEntity.badRequest().body("{\"error\": \"Invalid token\"}");
    }
}
