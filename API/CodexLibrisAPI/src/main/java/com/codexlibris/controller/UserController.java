/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;


import com.codexlibris.model.User;
import com.codexlibris.model.Role;
import com.codexlibris.dto.UserDTO;
import com.codexlibris.repository.RoleRepository;
import com.codexlibris.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author jessica
 */

@RestController
@RequestMapping("/users")
@Tag(name = "Usuaris", description = "Endpoints per a gestionar usuaris")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Operation(summary = "Obtenir tots els usuaris", description = "Retorna la llista completa de usuaris registrats.")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/")
    public String home() {
        return "API is running!";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un usuari", description = "Obtenir un usuari a partir de les dades proporcionades")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar un usuari a partir de les dades proporcionades")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    user.setLastLogin(userDetails.getLastLogin());
                    user.setIsActive(userDetails.getIsActive());
                    userRepository.save(user);
                    return ResponseEntity.ok(user);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear un nou usuari", description = "Crea un usuari amb les dades proporcionades en la solicitut.")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        
        User authUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Error: No s'ha trobat l'usuari autenticat"));
        
        if (authUser.getRole().getId() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat: Només els administradors poden crear usuaris.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El email ja está registrat.");
        }
        
        List<Integer> allowedRoles = List.of(1, 2);
        if (!allowedRoles.contains(userDTO.getRoleId())) {
            throw new RuntimeException("Error: El rol introduït no és vàlid");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Error: El rol no existeix"));


        User user = new User();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsActive(true);
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
