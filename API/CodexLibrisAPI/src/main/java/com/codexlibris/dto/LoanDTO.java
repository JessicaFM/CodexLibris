package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 *
 * @author jessica
 */
@Data
public class LoanDTO {

    @NotNull(message = "La data de reserva és obligatòria")
    private LocalDate loan_date;

    @NotNull(message = "La data de fi de reserva és obligatòria")
    private LocalDate due_date;

    @NotNull(message = "La data del préstec no pot ser nul·la")
    private LocalDate return_date;

    @NotNull(message = "El usuari de la reserva es obligatori")
    private Integer userId;

    @NotNull(message = "El llibre de la reserva es obligarori")
    private Integer bookId;

    @NotNull(message = "El estatus de la reserva es obligatori")
    private Integer statusId;
}
