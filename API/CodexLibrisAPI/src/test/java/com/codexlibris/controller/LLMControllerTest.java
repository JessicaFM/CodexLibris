/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jessica
 */
@SpringBootTest
public class LLMControllerTest {
    @Autowired
    private LLMController llmController;

    @MockBean
    private RestTemplate restTemplate;
    
    @Test
    void testRecomenderBook() {
        String inputText = "M'agrada la fantasia i les novel·les màgiques";
        String expectedResponse = "Et recomano 'Harry Potter i la pedra filosofal'.";

        ReflectionTestUtils.setField(llmController, "flaskApiUrl", "http://mock-llm/api");

        ResponseEntity<String> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),                 
                any(HttpEntity.class),     
                any(Class.class)       
        )).thenReturn(mockResponse);

        ResponseEntity<?> response = llmController.recomenderBook(Map.of("text", inputText));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
    
    @Test
    void testRecomenderBookEmpty() {
        String inputText = "Simular error";

    ReflectionTestUtils.setField(llmController, "flaskApiUrl", "http://mock-llm/api");

    // Simulamos que el servidor lanza excepción
    when(restTemplate.postForEntity(
            anyString(),
            any(HttpEntity.class),
            any(Class.class)
    )).thenThrow(new RuntimeException("Servidor no disponible"));

    // Act
    ResponseEntity<?> response = llmController.recomenderBook(Map.of("text", inputText));

    // Assert
    assertEquals(500, response.getStatusCodeValue());
    String body = response.getBody().toString();
    assertTrue(body.contains("Error connexió del servei LLM"));
    }
}
