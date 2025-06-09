package com.codexlibris.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookUpdateDTO {
    private String title;
    private String isbn;
    private Boolean available;
    private LocalDateTime publishedDate;
    private Integer authorId;
    private Integer genreId;
}