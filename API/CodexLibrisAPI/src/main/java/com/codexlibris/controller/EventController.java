/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.EventDTO;
import com.codexlibris.model.Event;
import com.codexlibris.model.User;
import com.codexlibris.repository.EventRepository;
import com.codexlibris.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "Endpoints per a la gestió de events")
public class EventController {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    public EventController(EventRepository eventRespository, UserRepository userRepository) {
        this.eventRepository = eventRespository;
        this.userRepository = userRepository;
    }
    
    @GetMapping
    @Operation(summary = "Obtenir el llistat de tots els esdeveniments")
    public List<Event> getAllEvents() { return eventRepository.findAll(); }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un esdeveniments a partir de un ID")
    public ResponseEntity<Event> getEventId(@PathVariable Integer id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    @Operation(summary = "Actualitzar un esdeveniment amb les dades proporcionades")
    public ResponseEntity<?> updateEvent(@PathVariable Integer id, @RequestBody EventDTO eventDTO) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No s'ha trobat l'esdeveniment amb l'ID especificat."));
        }

        Event event = optionalEvent.get();
        event.setDescription(eventDTO.getDescription());
        event.setTitle(eventDTO.getTitle());
        event.setLocation(eventDTO.getLocation());
        event.setEvent_date(eventDTO.getEvent_date());
        event.setStart_time(eventDTO.getStart_time());
        event.setEnd_time(eventDTO.getEnd_time());

        eventRepository.save(event);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    @Operation(summary = "Crear un nou esdeveniment", description = "Crear un nou esdeveniment amb les dades proporcionades")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDTO eventDTO, BindingResult result) {
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
        
        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setLocation(eventDTO.getLocation());
        event.setEvent_date(eventDTO.getEvent_date());
        event.setStart_time(eventDTO.getStart_time());
        event.setEnd_time(eventDTO.getEnd_time());

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un nou esdeveniment", description = "Eliminar un nou esdeveniment amb les dades proporcionades")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
