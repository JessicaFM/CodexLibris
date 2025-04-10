/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Genre;
import com.codexlibris.repository.GenreRepository;
import com.codexlibris.repository.UserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private UserRepository userRepository;

    // Test per comprovar que GET /genres retorna correctament tots els gèneres
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllGenres() throws Exception {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Ciència-ficció");

        when(genreRepository.findAll()).thenReturn(List.of(genre));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ciència-ficció"));
    }

    // Test per comprovar que GET /genres/{id} retorna un gènere existent
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetGenreById() throws Exception {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Fantasía");

        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));

        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fantasía"));
    }

    // Test per comprovar que GET /genres/{id} retorna 404 si el gènere no existeix
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGenreNotFound() throws Exception {
        when(genreRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/genres/999"))
                .andExpect(status().isNotFound());
    }
}
