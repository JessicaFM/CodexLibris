package com.codexlibris.dto;

import com.codexlibris.model.Book;
import com.codexlibris.model.LoanStatus;
import com.codexlibris.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 *
 * @author jessica
 */
@Data
public class LoanDTO {

    @NotBlank(message = "La data de reserva es obligatoria")
    private LocalDate loan_date;

    @NotBlank(message = "La data de fi de reserva es obligatoria")
    private LocalDate due_date;

    private LocalDate return_date;

    @NotBlank(message = "El usuari de la reserva es obligatori")
    private Integer userId;

    @NotBlank(message = "El llibre de la reserva es obligarori")
    private Integer bookId;

    @NotBlank(message = "El estatus de la reserva es obligatori")
    private Integer statusId;
}
