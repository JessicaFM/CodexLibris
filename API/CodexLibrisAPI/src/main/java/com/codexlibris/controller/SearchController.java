package com.codexlibris.controller;

import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.model.Book;
import com.codexlibris.model.Author;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/search")
@Tag(name = "Search internal books", description = "Endpoint to search Books & Authors")
public class SearchController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public SearchController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/")
    @Operation(summary = "Obtenir llibres o autors per string")
    public ResponseEntity<?> search(@RequestParam String query) {
        List<Book> books = bookRepository.searchByTitle(query);
        List<Author> authors = authorRepository.searchByAuthor(query);

        Map<String, Object> result = new HashMap<>();
        result.put("books", books);
        result.put("authors", authors);

        return ResponseEntity.ok(result);
    }
}
