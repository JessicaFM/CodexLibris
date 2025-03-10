/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.service;

import com.codexlibris.dto.UserDTO;
import com.codexlibris.model.User;
import com.codexlibris.repository.UserRepository;
import com.codexlibris.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author jessica
 */
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
   
    public User addUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setIsActive(true);
        user.setRole(roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado")));

        return userRepository.save(user);
    }
}
