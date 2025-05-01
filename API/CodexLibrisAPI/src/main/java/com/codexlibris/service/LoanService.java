package com.codexlibris.service;

import com.codexlibris.dto.GenreDTO;
import com.codexlibris.dto.LoanResponseDTO;
import com.codexlibris.model.Loan;
import com.codexlibris.model.LoanStatus;
import com.codexlibris.model.User;
import com.codexlibris.model.Book;
import com.codexlibris.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToLoanResponse)
                .toList();
    }
    
    public Optional<LoanResponseDTO> getLoanById(Integer id) {
        return loanRepository.findById(id)
                .map(this::convertToLoanResponse);
    }

    public LoanResponseDTO convertToLoanResponse(Loan loan) {
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();

        loanResponseDTO.setId(loan.getId());
        loanResponseDTO.setLoan_date(loan.getLoan_date());
        loanResponseDTO.setDue_date(loan.getDue_date());
        loanResponseDTO.setReturn_date(loan.getReturn_date());

        User user = loan.getUser();
        loanResponseDTO.setUser_id(user.getId());
        loanResponseDTO.setUser_name(user.getUsername());
        loanResponseDTO.setUser_first_name(user.getFirstName());
        loanResponseDTO.setUser_email(user.getEmail());

        Book book = loan.getBook();
        loanResponseDTO.setBook_id(book.getId());
        loanResponseDTO.setBook_title(book.getTitle());
        loanResponseDTO.setBook_isbn(book.getIsbn());

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setName(book.getGenre().getName());
        genreDTO.setDescription(book.getGenre().getDescription());
        loanResponseDTO.setGenre(genreDTO);

        LoanStatus status = loan.getLoanStatus();
        loanResponseDTO.setLoan_status_id(status.getId());
        loanResponseDTO.setLoan_status_name(status.getName());

        return loanResponseDTO;
    }
    
    

}
