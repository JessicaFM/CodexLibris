package com.codexlibris.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 *
 * @author jessica
 */
@Data
public class LoanResponseDTO {

    private Integer id;
    private LocalDate loan_date;
    private LocalDate due_date;
    private LocalDate return_date;

    private Integer user_id;
    private String user_name;
    private String user_first_name;
    private String user_email;

    private Integer book_id;
    private String book_title;
    private String book_isbn;

    private GenreDTO genre;

    private Integer loan_status_id;
    private String loan_status_name;
}
