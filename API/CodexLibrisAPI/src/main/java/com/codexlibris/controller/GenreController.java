/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.GenreDTO;
import com.codexlibris.model.Genre;
import com.codexlibris.model.User;
import com.codexlibris.repository.GenreRepository;
import com.codexlibris.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/genres")
@Tag(name = "Genres", description = "Endpoints per a la gestio de gènere de llibres")
public class GenreController {

    private final GenreRepository genreRepository;
    private final UserRepository userRepository;

    public GenreController(GenreRepository genreRepository, UserRepository userRepository) {
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Obtenir llistat del gèneres de llibres disponibles")
    public List<Genre> getAllGenres() { return genreRepository.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un gènere a partir de un ID")
    public ResponseEntity<Genre> getGenreById(@PathVariable Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    @Operation(summary = "Actualizar un genere a partir de les dades proporcionades")
    public ResponseEntity<Genre> updateGenre(@PathVariable Integer id, @RequestBody Genre genreDetails) {
        return genreRepository.findById(id)
                .map(genre -> {
                    genre.setName(genreDetails.getName());

                    if(genreDetails.getDescription() != null) {
                        genre.setDescription(genreDetails.getDescription());
                    }
                    
                    genre.setUpdated_at(LocalDateTime.now());
                    
                    genreRepository.save(genre);
                    return ResponseEntity.ok(genre);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un nou gènere", description = "Crea un nou gènere a partir de les dades proporcionades")
    public ResponseEntity<?> createGenre(@Valid @RequestBody GenreDTO genreDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genre.setDescription(genreDTO.getDescription());

        try {
            Genre savedGenre = genreRepository.save(genre);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al desar el gènere");
        }
    }
}