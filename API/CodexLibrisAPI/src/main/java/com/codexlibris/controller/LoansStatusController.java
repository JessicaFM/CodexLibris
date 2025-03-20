package com.codexlibris.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/loans_status")
@Tag(name = "Loans Status", description = "Endpoint per a la gestió dels estatus de la reserva")
public class LoansStatusController {
}
