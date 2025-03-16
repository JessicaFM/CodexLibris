package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author jessica
 */
@Data
public class GenreDTO {

    @NotBlank(message = "El nom del gènere es obligaori")
    private String name;

    private String description;
}
