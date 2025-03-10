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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints per a gestionar permissos, inici i tancament de sessions")

public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "Logejar un usuari", description = "Logejar un usuari amb el username i el seu password.")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("🟢 1 - Debug: Username recibido -> " + request.getUsername());
        System.out.println("🟢 Debug: Password recibido -> " + request.getPassword());
    
        logger.info("🟢 1 - Recibida solicitud de login para usuario: {}", request.getUsername());

        
        try {
            AuthResponse authResponse = authService.authenticate(request.getUsername(), request.getPassword());
            logger.info("🟢 10 - Respuesta enviada con token: {}", authResponse.getToken());

            return ResponseEntity.ok().body(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Usuari o contrasenya incorrectes"));
        } catch (Exception e) {
            logger.error("🔴 Error en login: ", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Error intern del servidor"));
        }
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Tancar sessió", description = "Tancar sessió de un usuari a partir del token")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtService.blacklistToken(token);
            return ResponseEntity.ok().body("{\"message\": \"Has tancat la sessió correctament.\"}");
        }

        return ResponseEntity.badRequest().body("{\"error\": \"Token no vàlid\"}");
    }
}
