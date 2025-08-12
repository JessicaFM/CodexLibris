package com.codexlibris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BookUpdateDTO {
    private Integer id; 
    
    private String title;
    private String isbn;
    private Boolean available;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime publishedDate;
    
    private Integer authorId;
    private Integer genreId;
}