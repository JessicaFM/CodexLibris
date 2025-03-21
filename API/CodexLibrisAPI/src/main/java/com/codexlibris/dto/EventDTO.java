package com.codexlibris.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author jessica
 */
@Data
public class EventDTO {

    @NotBlank(message = "El titol es obligatori")
    private String title;

    private String description;

    private String location;

    @NotBlank(message = "La data del event es obligatoria")
    private LocalDate event_date;

    private LocalTime start_time;

    private LocalTime end_time;
}
