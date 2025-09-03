/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.BookDTO;
import com.codexlibris.dto.BookDetailsDTO;
import com.codexlibris.dto.BookUpdateDTO;
import com.codexlibris.model.Author;
import com.codexlibris.model.Book;
import com.codexlibris.model.Genre;
import com.codexlibris.model.User;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.repository.GenreRepository;
import com.codexlibris.repository.UserRepository;
import com.codexlibris.service.BookService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Endpoints per a la gestió de llibres")
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;


    public BookController(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            UserRepository userRepository,
            BookService bookService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Obtenir el llistat de tots els llibres")
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        List<BookDTO> body = bookService.getAllBooks(offset, limit);
        return ResponseEntity.ok(body);
    }

    @Timed(
        value = "books.get.by_id",
        description = "Latency at GET /books/{id}",
        histogram = true,
        percentiles = {0.5, 0.95, 0.99}
    )
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un llibre a partir de un ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Integer id) {
        return bookRepository.findById(id)
            .map(book -> {
                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setIsbn(book.getIsbn());
                dto.setGenreId(book.getGenre().getId());
                dto.setAuthorId(book.getAuthor().getId());
                dto.setAvailable(book.getAvailable());
                dto.setPublishedDate(book.getPublished_date().atOffset(ZoneOffset.UTC));
                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar un llibre a partir de les dades proporcionades")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Integer id, @RequestBody BookUpdateDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDTO.getTitle());
                    book.setIsbn(bookDTO.getIsbn());
                    book.setAvailable(bookDTO.getAvailable());
                    book.setPublished_date(bookDTO.getPublishedDate().toLocalDateTime());

                    authorRepository.findById(bookDTO.getAuthorId()).ifPresent(book::setAuthor);
                    genreRepository.findById(bookDTO.getGenreId()).ifPresent(book::setGenre);

                    book.setUpdated_at(LocalDateTime.now());

                    Book updated = bookRepository.save(book);

                    BookDTO dto = new BookDTO();
                    dto.setTitle(updated.getTitle());
                    dto.setIsbn(updated.getIsbn());
                    dto.setGenreId(updated.getGenre().getId());
                    dto.setAuthorId(updated.getAuthor().getId());
                    dto.setAvailable(updated.getAvailable());
                    dto.setPublishedDate(book.getPublished_date().atOffset(ZoneOffset.UTC));

                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nou llibre", description = "Crear un nou llibre amb les dades proporcionades")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: No s'ha trobat l'usuari autenticat"));

        if (authUser.getRole().getId() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accés denegat: Només els administradors poden crear nous llibres.");
        }

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Error: El autor introduït no existeix"));

        Genre genre = genreRepository.findById(bookDTO.getGenreId())
                .orElseThrow(() -> new RuntimeException("Error: El genere introduït no existeix"));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(genre);
        book.setAvailable(bookDTO.getAvailable());
        book.setPublished_date(bookDTO.getPublishedDate().toLocalDateTime());

        Book savedBook = bookRepository.save(book);

        BookDTO dto = new BookDTO();
        dto.setTitle(savedBook.getTitle());
        dto.setIsbn(savedBook.getIsbn());
        dto.setGenreId(savedBook.getGenre().getId());
        dto.setAuthorId(savedBook.getAuthor().getId());
        dto.setAvailable(savedBook.getAvailable());
        dto.setPublishedDate(book.getPublished_date().atOffset(ZoneOffset.UTC));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping("/{id}/details")
    public ResponseEntity<BookDetailsDTO> getBookDetailsBydId(@PathVariable int id) {
        return bookRepository.findById(id)
            .map(book -> {
                BookDetailsDTO dto = new BookDetailsDTO();
                dto.setTitle(book.getTitle());
                dto.setIsbn(book.getIsbn());
                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @CacheEvict(value = "books", allEntries = true)
    @GetMapping("/clear-cache")
    @Operation(summary = "Neteja la memòria cau de llibres")
    public ResponseEntity<String> clearBooksCache() {
        return ResponseEntity.ok("✅ Caché de llibres buidada correctament");
    }
}
