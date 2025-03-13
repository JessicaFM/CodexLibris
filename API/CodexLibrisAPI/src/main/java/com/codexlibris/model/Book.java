package com.codexlibris.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.LocalVariable;

import java.time.LocalDateTime;

/**
 *
 * @author jessica
 */

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "isbn", nullable = false, length = 20, unique = true)
    private String isbn;

    @Column(name = "published_date")
    private LocalDateTime published_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;

    public Author getAuthor() { return author; }

    public Genre getGenre() { return genre; }

    public Book(String title, Author author, String isbn, LocalDateTime published_date, Genre genre, Boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.published_date = published_date;
        this.genre = genre;
        this.available = available;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
