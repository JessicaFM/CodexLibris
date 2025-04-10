/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.controller;

import com.codexlibris.dto.LoanResponseDTO;
import com.codexlibris.model.Loan;
import com.codexlibris.repository.*;
import com.codexlibris.service.LoanService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author jessica
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LoanStatusRepository loanStatusRepository;

    @MockBean
    private LoanService loanService;

    // Test per comprovar que l'endpoint GET /loans retorna correctament totes les reserves
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllLoans() throws Exception {
        LoanResponseDTO loan = new LoanResponseDTO();
        loan.setId(1);
        loan.setLoan_date(LocalDate.of(2024, 4, 10));
        loan.setDue_date(LocalDate.of(2024, 4, 20));
        loan.setBook_title("El Principito");
        loan.setBook_id(100);
        loan.setUser_id(10);
        loan.setUser_name("Saint-Exupéry");
        loan.setUser_first_name("Antoine");
        loan.setUser_email("antoine@literatura.com");
        loan.setLoan_status_id(1);
        loan.setLoan_status_name("Activa");

        when(loanService.getAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].book_title").value("El Principito"))
                .andExpect(jsonPath("$[0].user_first_name").value("Antoine"))
                .andExpect(jsonPath("$[0].loan_status_name").value("Activa"));
    }

    // Test per comprovar que GET /loans/{id} retorna una reserva existent
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetLoanById() throws Exception {
        Loan loan = new Loan();
        loan.setId(1);
        loan.setLoan_date(LocalDate.of(2024, 4, 10));
        loan.setDue_date(LocalDate.of(2024, 4, 20));

        when(loanRepository.findById(1)).thenReturn(Optional.of(loan));

        mockMvc.perform(get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    
    // Test per comprovar que GET /loans/{id} retorna 404 si la reserva no existeix
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetLoanById_NotFound() throws Exception {
        when(loanRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/loans/999"))
                .andExpect(status().isNotFound());
    }
}
