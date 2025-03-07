/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codexlibris.model.User;
import java.util.Optional;

/**
 *
 * @author jessica
 */
public interface UserRepository extends JpaRepository<User, Long> {    
    Optional<User> findByUserName(String userName);
}
