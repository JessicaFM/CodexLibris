package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 *
 * @author jessica
 */
@Data
public class LoanStatusDTO {

    @NotBlank(message = "El nom del estatus de la reserva es obligatoria")
    private String name;

    private String description;
}
