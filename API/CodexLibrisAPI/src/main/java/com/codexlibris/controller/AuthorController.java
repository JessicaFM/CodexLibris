package com.codexlibris.controller;

import com.codexlibris.dto.AuthorDTO;
import com.codexlibris.model.Author;
import com.codexlibris.model.User;
import com.codexlibris.repository.AuthorRepository;
import com.codexlibris.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "Actualitzar un autor amb les dades proporcionades")
    public ResponseEntity<Author> updateAuthor(@PathVariable Integer id, @RequestBody Author authorDetails) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDetails.getName());
                    author.setBirth_date((authorDetails.getBirth_date()));
                    author.setNationality(authorDetails.getNationality());
                    authorRepository.save(author);
                    return ResponseEntity.ok(author);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nou autor", description = "Crear un autor amb les dade proporcionades en la solicitut.")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorDTO authorDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User authUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: No s'ha trobat l'usuari autenticat"));

        if (authUser.getRole().getId() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat: Només els administradors poden crear nous autor.");
        }

        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBirth_date(authorDTO.getBirthDate());
        author.setNationality(authorDTO.getNationality());

        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(savedAuthor);
    }
}
