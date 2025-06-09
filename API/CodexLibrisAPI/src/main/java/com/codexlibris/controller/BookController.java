/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.BookDTO;
import com.codexlibris.dto.BookUpdateDTO;
import com.codexlibris.model.Author;
import com.codexlibris.model.Book;
import com.codexlibris.model.Genre;
import com.codexlibris.model.User;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.repository.GenreRepository;
import com.codexlibris.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Obtenir el lllistat de tots els llibres")
    public  List<Book> getAllBooks() { return bookRepository.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un llibre a partir de un ID")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar un llibre a partir de les dades proporcionades")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody BookUpdateDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDTO.getTitle());
                    book.setIsbn(bookDTO.getIsbn());
                    book.setAvailable(bookDTO.getAvailable());
                    book.setPublished_date(bookDTO.getPublishedDate());

                    // buscar entidades por ID
                    authorRepository.findById(bookDTO.getAuthorId()).ifPresent(book::setAuthor);
                    genreRepository.findById(bookDTO.getGenreId()).ifPresent(book::setGenre);

                    book.setUpdated_at(LocalDateTime.now());

                    bookRepository.save(book);
                    return ResponseEntity.ok(book);
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat: Només els administradors poden crear nous llibres.");
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
        book.setPublished_date(bookDTO.getPublishedDate());

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
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

}
