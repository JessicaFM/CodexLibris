package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author jessica
 */
@Data
public class BookDTO {

    @NotBlank(message = "El títol del llibre es obligatori")
    private String title;

    @NotNull(message = "El autor del llibre es obligatori")
    private Integer authorId;

    @NotBlank(message = "El codi ISBN es obligatori")
    private String isbn;

    private LocalDateTime publishedDate;

    @NotBlank(message = "El genere del llibre es obligatori")
    private Integer genreId;

    private Boolean available;
}
