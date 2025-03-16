package com.codexlibris.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author jessica
 */
@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
    
    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
