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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Loan> getLoanById(@PathVariable Integer id) {
        Optional<Loan> loans = loanRepository.findById(id);
        return loans.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiztar una reserva a partir de les dades proporcionades")
    public ResponseEntity<Loan> updateLoan(@PathVariable Integer id, @RequestBody Loan loanDetails){
        return loanRepository.findById(id)
                .map(loan -> {
                    loan.setUser(loanDetails.getUser());
                    loan.setBook(loanDetails.getBook());
                    loan.setLoan_date(loanDetails.getLoan_date());
                    loan.setDue_date(loanDetails.getDue_date());
                    loan.setReturn_date(loanDetails.getDue_date());
                    loan.setUpdated_at(LocalDateTime.now());

                    loanRepository.save(loan);
                    return ResponseEntity.ok(loan);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nova reserva", description = "Crear una nova reerva amb les dades proporcionades")
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanDTO loanDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Error: el llibre no existeix"));

        User user = userRepository.findById(loanDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: l'usuari de assignació de la reserva no existeix"));

        LoanStatus loanStatus = loanStatusRepository.findById(loanDTO.getStatusId())
                .orElseThrow(() -> new RuntimeException("Error: el estatus de la reserva no existeix"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanStatus(loanStatus);
        loan.setLoan_date(loanDTO.getLoan_date());
        loan.setDue_date(loanDTO.getLoan_date());

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

}