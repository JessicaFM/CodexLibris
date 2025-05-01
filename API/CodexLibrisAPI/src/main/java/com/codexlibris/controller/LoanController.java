package com.codexlibris.controller;

import com.codexlibris.dto.LoanDTO;
import com.codexlibris.dto.LoanResponseDTO;
import com.codexlibris.model.Book;
import com.codexlibris.model.Loan;
import com.codexlibris.model.LoanStatus;
import com.codexlibris.model.User;
import com.codexlibris.repository.BookRepository;
import com.codexlibris.repository.LoanRepository;
import com.codexlibris.repository.LoanStatusRepository;
import com.codexlibris.repository.UserRepository;
import com.codexlibris.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 *
 * @author jessica
 */
@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Endpoints per a la gestio de les reserves de llibres")
public class LoanController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final LoanService loanService;

    public LoanController(BookRepository bookRepository, UserRepository userRepository, LoanRepository loanRepository, LoanStatusRepository loanStatusRepository, LoanService loanService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.loanStatusRepository = loanStatusRepository;
        this.loanService = loanService;
    }

    @GetMapping
    @Operation(summary = "Obtenir el lllistat de totes les reserves")
    public List<LoanResponseDTO> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir una reserva a partir de un ID")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar una reserva amb les dades proporcionades")
    public ResponseEntity<?> updateLoan(@PathVariable Integer id, @Valid @RequestBody LoanDTO loanDTO) {
    return loanRepository.findById(id)
        .map(existingLoan -> {

            boolean alreadyExists = loanRepository.existsByUserIdAndBookIdAndIdNot(
                    loanDTO.getUserId(), loanDTO.getBookId(), id
            );

            if (alreadyExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Ja existeix una altra reserva amb aquest llibre i usuari."));
            }

            boolean overlapping = loanRepository.existsOverlappingLoanForBook(
                    loanDTO.getBookId(),
                    loanDTO.getLoan_date(),
                    loanDTO.getDue_date(),
                    id
            );

            if (overlapping) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Aquest llibre ja està reservat en aquest període."));
            }

            Book book = bookRepository.findById(loanDTO.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Error: el llibre no existeix"));

            User user = userRepository.findById(loanDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Error: l'usuari no existeix"));

            LoanStatus loanStatus = loanStatusRepository.findById(loanDTO.getStatusId())
                    .orElseThrow(() -> new IllegalArgumentException("Error: l'estat de la reserva no existeix"));

            existingLoan.setBook(book);
            existingLoan.setUser(user);
            existingLoan.setLoanStatus(loanStatus);
            existingLoan.setLoan_date(loanDTO.getLoan_date());
            existingLoan.setDue_date(loanDTO.getDue_date());
            existingLoan.setReturn_date(loanDTO.getReturn_date());

            Loan updatedLoan = loanRepository.save(existingLoan);
            LoanResponseDTO response = loanService.convertToLoanResponse(updatedLoan); // <-- usa método del service

            return ResponseEntity.ok(response);

        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No s'ha trobat la reserva amb l'id especificat.")));
    }


    @PostMapping
    @Operation(summary = "Crear una nova reserva", description = "Crear una nova reserva amb les dades proporcionades")
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanDTO loanDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        boolean alreadyReserved = loanRepository.existsByUserIdAndBookId(
                loanDTO.getUserId(), loanDTO.getBookId());

        if (alreadyReserved) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Aquest usuari ja té una reserva activa per aquest llibre."));
        }

        boolean overlapping = loanRepository.existsOverlappingLoanForBook(
                loanDTO.getBookId(),
                loanDTO.getLoan_date(),
                loanDTO.getDue_date(),
                -1
        );

        if (overlapping) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Aquest llibre ja està reservat en aquest període."));
        }

        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Error: el llibre no existeix"));

        User user = userRepository.findById(loanDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Error: l'usuari de la reserva no existeix"));

        LoanStatus loanStatus = loanStatusRepository.findById(loanDTO.getStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Error: l'estat de la reserva no existeix"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanStatus(loanStatus);
        loan.setLoan_date(loanDTO.getLoan_date());
        loan.setDue_date(loanDTO.getDue_date());
        loan.setReturn_date(loanDTO.getReturn_date());

        Loan savedLoan = loanRepository.save(loan);
        return ResponseEntity.ok(savedLoan);
    }

    @Operation(summary = "Tancar una reserva", description = "Marcar la reserva com a retornada")
    @PutMapping("/loan/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Integer id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: la reserva no existeix"));

        if (loan.getReturn_date() != null) {
            return ResponseEntity.badRequest().body("La reserva ja ha estat retornada");
        }

        loan.setReturn_date(LocalDate.now());

        LoanStatus returnedStatus = loanStatusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error: el estatus no existeix"));
        loan.setLoanStatus(returnedStatus);

        loanRepository.save(loan);
        return ResponseEntity.ok("Reserva retornada correctament");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        if (loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}