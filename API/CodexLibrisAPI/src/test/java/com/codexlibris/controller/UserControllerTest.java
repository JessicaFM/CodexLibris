
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Role;
import com.codexlibris.model.User;
import com.codexlibris.repository.RoleRepository;
import com.codexlibris.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        Role adminRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no trobat"));
        userRepository.save(new User("admin", "Admin", "User", "admin@example.com", "password123", true, adminRole));
        
        Role userRole = roleRepository.findById(2)
            .orElseThrow(() -> new RuntimeException("Error: Rol USER no trobat"));
        userRepository.save(new User("user", "Usuari", "Normal", "user@example.com", "password123", true, userRole));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"}) // 🔥 Simulem un usuari amb rol USER
    void testCreateUserForbidden() throws Exception {
        // 🔥 Intentem fer una petició com a USER, ha de donar 403 Forbidden
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"userName\": \"nouusuari\", \"firstName\": \"Nou\", \"lastName\": \"Usuari\", \"email\": \"nou@example.com\", \"password\": \"123456\", \"roleId\": 2 }"))
                .andExpect(status().isForbidden()); // 🔥 Esperem un 403 Forbidden
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testCreateUserSuccess() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userName\": \"nouusuari\", \"firstName\": \"Nou\", \"lastName\": \"Usuari\", \"email\": \"nou@example.com\", \"password\": \"123456\", \"roleId\": 2 }"))
                .andExpect(status().isOk());
    }
}
