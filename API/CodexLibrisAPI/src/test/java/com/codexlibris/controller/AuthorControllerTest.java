/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Author;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.UserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private UserRepository userRepository;

    // Test per comprovar que GET /authors retorna correctament la llista d'autors
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllAuthors() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setName("Gabriel García Márquez");

        when(authorRepository.findAll()).thenReturn(List.of(author));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gabriel García Márquez"));
    }
    
    // Test per comprovar que GET /authors/{id} retorna un autor existent
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAuthorById() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setName("Isabel Allende");
        author.setBirth_date(LocalDate.of(1942, 8, 2));

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Isabel Allende"));
    }

    // Test per comprovar que GET /authors/{id} retorna 404 si l'autor no existeix
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAuthorNotFound() throws Exception {
        when(authorRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/authors/999"))
                .andExpect(status().isNotFound());
    }
}
