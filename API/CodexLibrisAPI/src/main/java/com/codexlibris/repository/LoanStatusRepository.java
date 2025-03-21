package com.codexlibris.repository;

import com.codexlibris.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jessica
 */
@Repository
public interface LoanStatusRepository extends JpaRepository<LoanStatus, Integer> { }
