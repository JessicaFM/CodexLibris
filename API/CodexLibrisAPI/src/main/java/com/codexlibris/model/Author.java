package com.codexlibris.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author jessica
 */

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birth_date;

    @Column(name = "nationality")
    private String nationality;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @UpdateTimestamp 
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;
    
    public Author(String name, LocalDate birth_date, String nationality) {
        this.name = name;
        this.birth_date = birth_date;
        this.nationality = nationality;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}
