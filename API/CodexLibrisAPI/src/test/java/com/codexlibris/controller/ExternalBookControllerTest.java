/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.BookSearchExternalDTO;
import com.codexlibris.dto.BookSearchExternalResponseDTO;
import com.codexlibris.service.OpenLibraryClient;
import java.util.List;
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
public class ExternalBookControllerTest {
    
    @Autowired
    private ExternalBookController externalBookController;

    @MockBean
    private OpenLibraryClient openLibraryClient;
    
     @Test
    void testSearchBooksResponseStatus() {
        String query = "harry potter";
        BookSearchExternalResponseDTO mockResponse = new BookSearchExternalResponseDTO();

        when(openLibraryClient.searchBooks(query)).thenReturn(mockResponse);

        ResponseEntity<?> response = externalBookController.searchBooks(query);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }
    
    @Test
    void testSearchBooksContent() {
        String query = "harry potter";

        // Creamos libros simulados
        BookSearchExternalDTO book1 = new BookSearchExternalDTO();
        book1.setTitle("Harry Potter vol.1");

        BookSearchExternalDTO book2 = new BookSearchExternalDTO();
        book2.setTitle("Harry Potter vol.2");

        BookSearchExternalResponseDTO mockResponse = new BookSearchExternalResponseDTO();
        mockResponse.setTotal(2);
        mockResponse.setResults(List.of(book1, book2));

        when(openLibraryClient.searchBooks(query)).thenReturn(mockResponse);

        ResponseEntity<?> response = externalBookController.searchBooks(query);

        assertEquals(200, response.getStatusCodeValue());

        BookSearchExternalResponseDTO body = (BookSearchExternalResponseDTO) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getTotal());
        assertEquals(2, body.getResults().size());
        assertEquals("Harry Potter vol.1", body.getResults().get(0).getTitle());
    }
}
