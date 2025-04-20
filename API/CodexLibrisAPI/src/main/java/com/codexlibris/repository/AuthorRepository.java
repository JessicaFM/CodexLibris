package com.codexlibris.repository;

import com.codexlibris.model.Author;
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
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Author> searchByAuthor(@Param("query") String query);

}
