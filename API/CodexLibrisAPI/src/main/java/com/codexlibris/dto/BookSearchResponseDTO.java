package com.codexlibris.dto;

import lombok.Data;

import javax.print.Doc;
import java.util.List;

@Data
public class BookSearchResponseDTO {

    private List<Doc> docs;

    @Data
    public static class Doc {
        private String title;
        private List<String> author_name;
        private String first_publish_year;
        private String isbn;
        private String cover_i;

        public String getCoverUrl() {
            if (cover_i != null && !cover_i.isEmpty()) {
                return "https://covers.openlibrary.org/b/id/" + cover_i + "-L.jpg";
            } else {
                return null;
            }
        }
    }
}
