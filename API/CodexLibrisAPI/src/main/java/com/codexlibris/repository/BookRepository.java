package com.codexlibris.repository;

import com.codexlibris.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author jessica
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
/*
    @Query("SELECT b FROM books b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchByTitle(@Param("query") String query);
*/
}
