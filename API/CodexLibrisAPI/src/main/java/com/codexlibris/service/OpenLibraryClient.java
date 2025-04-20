package com.codexlibris.service;

import com.codexlibris.dto.BookSearchExternalDTO;
import com.codexlibris.dto.BookSearchExternalResponseDTO;
import com.codexlibris.dto.OpenLibraryResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 *
 * @author jessica
 */

@Service
public class OpenLibraryClient {

    private final WebClient webClient;

    public OpenLibraryClient(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("https://openlibrary.org")
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(4 * 1024 * 1024)) // 4 MB
                .build())
            .build();
    }

    public BookSearchExternalResponseDTO searchBooks(String query) {
        OpenLibraryResponseDTO rawResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("q", query)
                        .queryParam("limit", 1000)
                        .queryParam("language", "spa")
                        .build())
                .retrieve()
                .bodyToMono(OpenLibraryResponseDTO.class)
                .block();

        List<BookSearchExternalDTO> simplified = rawResponse.getDocs().stream()
            .filter(doc -> {
                String lowerQuery = query.toLowerCase();

                boolean matchesTitle = doc.getTitle() != null &&
                    doc.getTitle().toLowerCase().contains(lowerQuery);

                boolean matchesAuthor = doc.getAuthorNames() != null &&
                    doc.getAuthorNames().stream()
                        .anyMatch(name -> name.toLowerCase().contains(lowerQuery));

                return matchesTitle || matchesAuthor;
            })
            .map(doc -> {
                BookSearchExternalDTO dto = new BookSearchExternalDTO();
                dto.setTitle(doc.getTitle());
                dto.setAuthor(doc.getAuthorNames() != null && !doc.getAuthorNames().isEmpty()
                        ? doc.getAuthorNames().get(0)
                        : "Desconegut");
                dto.setYear(doc.getFirstPublishYear());
                dto.setIsbn(doc.getIsbns() != null && !doc.getIsbns().isEmpty()
                        ? doc.getIsbns().get(0)
                        : "N/D");
                return dto;
            })
            .toList();

        BookSearchExternalResponseDTO finalResponse = new BookSearchExternalResponseDTO();
        finalResponse.setTotal(rawResponse.getNumFound());
        finalResponse.setResults(simplified);
        return finalResponse;
    }
}
