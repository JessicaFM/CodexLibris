/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Author;
import com.codexlibris.model.Book;
import com.codexlibris.model.Genre;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.repository.GenreRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();
        
        LocalDate publishedDate = LocalDate.of(2023, 1, 1);
        
        Genre fantasy = new Genre("Fantasía", "Històries ambientades en mons màgics");
        fantasy = genreRepository.save(fantasy);
        System.out.println("Genre ID: " + fantasy.getId());


        Author author1 = authorRepository.save(new Author("J.K. Rowling", publishedDate, "Regne Unit"));

        // Crear els llibres
        Book book1 = new Book("Harry Potter i la pedra filosofal", author1, "123456789", LocalDateTime.of(1997, 6, 26, 0, 0), fantasy, true);
        bookRepository.save(book1);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Harry Potter i la pedra filosofal"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetBookById() throws Exception {
        Book book = bookRepository.findAll().get(0);
        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter i la pedra filosofal"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }
}