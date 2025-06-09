/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.demo.integration.AbstractIntegrationTest;
import com.codexlibris.model.Author;
import com.codexlibris.model.Book;
import com.codexlibris.model.Genre;
import com.codexlibris.model.Role;
import com.codexlibris.model.User;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.repository.GenreRepository;
import com.codexlibris.repository.RoleRepository;
import com.codexlibris.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.security.test.context.support.WithMockUser;

/**
 *
 * @author jessica
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private Integer authorId;
    private Integer genreId;
    
    // Configura les dades abans de cada test (neteja i crea un llibre amb autor i gènere)
    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
        
        LocalDate publishedDate = LocalDate.of(2023, 1, 1);
        
        Genre fantasy = new Genre("Fantasía", "Històries ambientades en mons màgics");
        fantasy = genreRepository.save(fantasy);
        genreId = fantasy.getId();
        System.out.println("Genre ID: " + fantasy.getId());


        Author author1 = authorRepository.save(new Author("J.K. Rowling", publishedDate, "Regne Unit"));
        authorId = author1.getId();
    
        // Crear els llibres
        Book book1 = new Book("Harry Potter i la pedra filosofal", author1, "123456789", LocalDateTime.of(1997, 6, 26, 0, 0), fantasy, true);
        bookRepository.save(book1);
        
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("1234");
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(adminRole);
        adminUser.setIsActive(true);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");

        userRepository.save(adminUser);
    }

    // Test per comprovar que GET /books retorna tots els llibres
    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Harry Potter i la pedra filosofal"));
    }

    // Test per comprovar que GET /books/{id} retorna un llibre existent
    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetBookById() throws Exception {
        Book book = bookRepository.findAll().get(0);
        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter i la pedra filosofal"));
    }

    // Test per comprovar que GET /books/{id} retorna 404 si el llibre no existeix
    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateBook() throws Exception {
        String json = """
        {
            "title": "Nuevo libro",
            "authorId": %d,
            "isbn": "987654321",
            "publishedDate": "2022-01-01T00:00:00",
            "genreId": %d,
            "available": true
        }
        """;

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nuevo libro"))
                .andDo(result -> {
    System.out.println("RESPONSE STATUS: " + result.getResponse().getStatus());
    System.out.println("RESPONSE BODY: " + result.getResponse().getContentAsString());
});

    }

}