package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 *
 * @author jessica
 */
@Data
public class AuthorDTO {

    @NotBlank(message = "El nom del autor es obligatori")
    private String name;

    private LocalDate birthDate;

    private String nationality;
}
