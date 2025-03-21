package com.codexlibris.controller;

import com.codexlibris.dto.BookSearchResponseDTO;
import com.codexlibris.service.OpenLibraryClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/external-books")
@Tag(name = "External Books", description = "Endpoints per a la consulta externes de llibres")
public class ExternalBookController {

    @Autowired
    private OpenLibraryClient openLibraryClient;

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String q) {
        BookSearchResponseDTO response = openLibraryClient.searchBooks(q);
        return ResponseEntity.ok(response);
    }
}
