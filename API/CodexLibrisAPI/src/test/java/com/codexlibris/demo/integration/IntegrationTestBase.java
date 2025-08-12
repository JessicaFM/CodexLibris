/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.demo.integration;

import com.codexlibris.model.Role;
import com.codexlibris.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author jessica
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTestBase {
    
    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    void setupInitialData() {
        if (!roleRepository.existsById(1)) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrador");
            roleRepository.save(adminRole);
        }
    }
}