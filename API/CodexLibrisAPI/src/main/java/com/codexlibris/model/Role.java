/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author jessica
 */

@Entity
@Data
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<User> users;

    public Integer getId() { return id; }
    public String getName() { return name; }
    
}
