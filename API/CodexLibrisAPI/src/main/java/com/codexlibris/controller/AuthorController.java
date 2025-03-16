package com.codexlibris.controller;

import com.codexlibris.dto.AuthorDTO;
import com.codexlibris.model.Author;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/authors")
@Tag(name = "Authors", description = "Endoint per a la gestió de autors")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    public AuthorController(AuthorRepository authorRepository, UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Obtenir el llistat de tots autors")
    public List<Author> getAllAuthors() { return authorRepository.findAll(); }


    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un autor a partit de un ID")
    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    @Operation(summary = "Actualitzar un autor amb les dades proporcionades")
    public ResponseEntity<Author> updateAuthor(@PathVariable Integer id, @RequestBody Author authorDetails) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDetails.getName());
                    if (authorDetails.getBirth_date() != null) author.setBirth_date(authorDetails.getBirth_date());
                    if (authorDetails.getNationality() != null) author.setNationality(authorDetails.getNationality());
                    author.setUpdated_at(LocalDateTime.now()); 
                    
                    authorRepository.save(author);
                    return ResponseEntity.ok(author);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nou autor", description = "Crear un autor amb les dades proporcionades en la sol·licitud.")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Author author = new Author();
        author.setName(authorDTO.getName());
        
        if(authorDTO.getBirthDate() != null) {
            author.setBirth_date(authorDTO.getBirthDate());
        }
        
        if(authorDTO.getNationality() != null) {
            author.setNationality(authorDTO.getNationality());
        }

        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }
}
