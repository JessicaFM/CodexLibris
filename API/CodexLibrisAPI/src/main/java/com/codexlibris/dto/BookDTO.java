package com.codexlibris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
/**
 *
 * @author jessica
 */
@Data
public class BookDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id; 
    
    @NotBlank(message = "El títol del llibre es obligatori")
    private String title;

    @NotNull(message = "El autor del llibre es obligatori")
    private Integer authorId;

    @NotBlank(message = "El codi ISBN es obligatori")
    private String isbn;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX")
    private OffsetDateTime publishedDate;

    private Integer genreId;

    private Boolean available;
}
