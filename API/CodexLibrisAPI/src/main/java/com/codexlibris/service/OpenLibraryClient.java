package com.codexlibris.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.codexlibris.dto.BookSearchResponseDTO;

/**
 *
 * @author jessica
 */
/*
@Service
public class OpenLibraryClient {

    private final WebClient webClient;

    public OpenLibraryClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://openlibrary.org").build();
    }

    public BookSearchResponseDTO searchBooks(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("q", query)
                        .queryParam("language", "spa")
                        .build())
                .retrieve()
                .bodyToMono(BookSearchResponseDTO.class)
                .block();
    }
}
*/
