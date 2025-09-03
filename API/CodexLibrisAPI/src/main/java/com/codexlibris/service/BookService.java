package com.codexlibris.service;

import com.codexlibris.dto.BookDTO;
import com.codexlibris.model.Book;
import com.codexlibris.repository.BookRepository;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public  class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Cacheable(value = "books", key = "#offset + '-' + #limit")
  public List<BookDTO> getAllBooks(int offset, int limit) {
    int page = offset / limit;
    int skip = offset % limit;

    Pageable pageable = PageRequest.of(page, limit, Sort.by("title").ascending());
    Page<Book> bookPage = bookRepository.findAll(pageable);

    return bookPage.getContent().stream()
            .skip(skip)
            .limit(limit)
            .map(book -> {
              BookDTO dto = new BookDTO();
              dto.setId(book.getId());
              dto.setTitle(book.getTitle());
              dto.setIsbn(book.getIsbn());
              dto.setGenreId(book.getGenre().getId());
              dto.setAuthorId(book.getAuthor().getId());
              dto.setAvailable(book.getAvailable());
              dto.setPublishedDate(book.getPublished_date().atOffset(ZoneOffset.UTC));
              return dto;
            })
            .toList();
  }
}