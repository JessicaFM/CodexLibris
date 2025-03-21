package com.codexlibris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codexlibris.model.Loan;

/**
 *
 * @author jessica
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> { }