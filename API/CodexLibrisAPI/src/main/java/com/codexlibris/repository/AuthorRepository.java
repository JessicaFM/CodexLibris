package com.codexlibris.repository;

import com.codexlibris.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jessica
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {}
