package com.codexlibris.repository;

import com.codexlibris.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jessica
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {}
