/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.model.Event;
import com.codexlibris.repository.EventRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author jessica
 */
 
@SpringBootTest
public class EventControllerTest {

    @Autowired
    private EventRepository eventRepository;

    // Test bàsic per verificar la creació d’un objecte Event i accés als seus camps
    @Test
    void testCrearEvent() {
        Event event = new Event();
        event.setTitle("Presentació llibre");
        event.setDescription("Presentació del nou llibre");
        event.setLocation("Biblioteca Central");
        event.setEvent_date(LocalDate.of(2025, 4, 15));
        event.setStart_time(LocalTime.of(18, 0));
        event.setEnd_time(LocalTime.of(19, 30));

        assertEquals("Presentació llibre", event.getTitle());
        assertEquals("Biblioteca Central", event.getLocation());
        assertEquals(LocalDate.of(2025, 4, 15), event.getEvent_date());
    }
}