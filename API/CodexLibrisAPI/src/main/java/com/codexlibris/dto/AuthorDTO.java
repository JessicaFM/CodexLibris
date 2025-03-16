package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author jessica
 */
@Data
public class AuthorDTO {

    @NotBlank(message = "El nom del autor es obligatori")
    private String name;

    private LocalDateTime birthDate;

    private String nationality;
}
