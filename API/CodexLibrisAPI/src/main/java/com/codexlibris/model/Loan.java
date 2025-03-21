package com.codexlibris.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author jessica
 */

@Entity
@Table(name = "loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;

    @Column(name = "loan_date")
    private LocalDate loan_date;

    @Column(name = "due_date")
    private LocalDate due_date;

    @Column(name = "return_date")
    private LocalDate return_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = true)    
    private LoanStatus loanStatus;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;


    public User getUser() { return user; }

    public Book getBook() { return book; }

    public LoanStatus getLoanStatus() { return loanStatus; }

    public Loan(User user, Book book, LocalDate loan_date, LocalDate due_date) {
        this.user = user;
        this.book = book;
        this.loan_date = loan_date;
        this.due_date = due_date;
    }
}
