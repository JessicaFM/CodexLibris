/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Event;
import com.codexlibris.repository.EventRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventRepository eventRepository;
    
    public EventController(EventRepository eventRespository) {
        this.eventRepository = eventRespository;
    }
    
    @GetMapping
    @Operation(summary = "Obtenir el llistat de tots els esdeveniments")
    public List<Event> getAllEvents() { return eventRepository.findAll(); }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un llibre a partir de un ID")
    public ResponseEntity<Event> getBookById(@PathVariable Integer id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}
