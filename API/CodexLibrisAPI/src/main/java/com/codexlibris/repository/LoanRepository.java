package com.codexlibris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codexlibris.model.Loan;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jessica
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> { 
    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);
    
    boolean existsByUserIdAndBookIdAndIdNot(Integer userId, Integer bookId, Integer id);
    
    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM Loan l
        WHERE l.book.id = :bookId
        AND l.id <> :excludeId
        AND (
            (:startDate BETWEEN l.loan_date AND l.due_date)
            OR (:endDate BETWEEN l.loan_date AND l.due_date)
            OR (l.loan_date BETWEEN :startDate AND :endDate)
        )
    """)
    boolean existsOverlappingLoanForBook(@Param("bookId") Integer bookId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("excludeId") Integer excludeId);

}