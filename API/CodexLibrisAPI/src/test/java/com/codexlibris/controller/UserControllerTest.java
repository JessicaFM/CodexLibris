package com.codexlibris.controller;

import com.codexlibris.model.User;
import com.codexlibris.repository.RoleRepository;
import com.codexlibris.repository.UserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author jessica
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // Test per comprovar que GET /users retorna correctament tots els usuaris
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("jdoe");

        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jdoe"));
    }

    // Test per comprovar que GET /users/{id} retorna un usuari existent
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("janedoe");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janedoe"));
    }

    // Test per comprovar que GET /users/{id} retorna 404 si l’usuari no existeix
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUserNotFound() throws Exception {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    // Test per comprovar que GET /users/ (endpoint de prova) funciona correctament
    /*
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().string("API is running!"));
    }
*/
}
