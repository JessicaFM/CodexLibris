/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.AuthorSearchDTO;
import com.codexlibris.dto.BookSearchDTO;
import com.codexlibris.model.Author;
import com.codexlibris.model.Book;
import com.codexlibris.model.Genre;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jessica
 */
@SpringBootTest
public class SearchControllerTest {
    
    @Autowired
    private SearchController searchController;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;
    
     @Test
    void testSearch() {
        String query = "harry potter";

        Author author = new Author();
        author.setId(1);
        author.setName("J.K. Rowling");
        author.setNationality("Britànica");

        Genre genre = new Genre();
        genre.setName("Fantasia");

        Book book = new Book();
        book.setId(2);
        book.setTitle("Harry Potter i la pedra filosofal");
        book.setIsbn("9788478884452");
        book.setAvailable(true);
        book.setAuthor(author);
        book.setGenre(genre);

        when(bookRepository.searchByTitle(query)).thenReturn(List.of(book));
        when(authorRepository.searchByAuthor(query)).thenReturn(List.of(author));

        ResponseEntity<?> response = searchController.search(query);

        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> result = (Map<String, Object>) response.getBody();
        assertNotNull(result);

        List<BookSearchDTO> books = (List<BookSearchDTO>) result.get("books");
        List<AuthorSearchDTO> authors = (List<AuthorSearchDTO>) result.get("authors");

        assertEquals(1, books.size());
        BookSearchDTO bookDTO = books.get(0);
        assertEquals("Harry Potter i la pedra filosofal", bookDTO.getTitle());
        assertEquals("J.K. Rowling", bookDTO.getAuthorName());
        assertEquals("Fantasia", bookDTO.getGenreName());
        assertEquals("9788478884452", bookDTO.getIsbn());

        assertEquals(1, authors.size());
        AuthorSearchDTO authorDTO = authors.get(0);
        assertEquals("J.K. Rowling", authorDTO.getName());
        assertEquals("Britànica", authorDTO.getNationality());
    }
}
