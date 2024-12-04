package com.crud.CRUD.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenLibraryService {

    public List<Map<String, Object>> searchBooks(String query, String type) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://openlibrary.org/search.json?" + type + "=" + query;

        List<Map<String, Object>> books = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode docs = root.path("docs");

            for (JsonNode doc : docs) {
                Map<String, Object> book = new HashMap<>();
                book.put("title", doc.path("title").asText());
                book.put("author", doc.path("author_name").isArray() ? doc.path("author_name").get(0).asText() : "Unknown");
                book.put("year", doc.path("first_publish_year").asInt(0));

                // Añadir imágenes si están disponibles
                int coverId = doc.path("cover_i").asInt(0);
                if (coverId > 0) {
                    book.put("coverImage", "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                } else {
                    book.put("coverImage", "/images/default-book-cover.jpg"); // Imagen predeterminada
                }

                String authorKey = doc.path("author_key").isArray() ? doc.path("author_key").get(0).asText() : null;
                if (authorKey != null) {
                    book.put("authorImage", "https://covers.openlibrary.org/a/olid/" + authorKey + "-M.jpg");
                } else {
                    book.put("authorImage", "/images/default-author.jpg"); // Imagen predeterminada
                }

                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public Map<String, Object> searchAuthors(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://openlibrary.org/search/authors.json?q=" + query;

        Map<String, Object> authorData = new HashMap<>();
        List<Map<String, Object>> authors = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode docs = root.path("docs");

            for (JsonNode doc : docs) {
                Map<String, Object> author = new HashMap<>();
                author.put("name", doc.path("name").asText());
                author.put("birth_date", doc.path("birth_date").asText(null));
                author.put("top_work", doc.path("top_work").asText(null));
                author.put("work_count", doc.path("work_count").asInt(0));

                // Obtener trabajos del autor
                String authorKey = doc.path("key").asText();
                List<String> works = getWorksByAuthor(authorKey, 10).stream()
                        .map(work -> work.get("title").toString())
                        .toList();
                author.put("works", works);

                // Añadir imagen del autor
                if (authorKey != null && !authorKey.isEmpty()) {
                    author.put("authorImage", "https://covers.openlibrary.org/a/olid/" + authorKey + "-M.jpg");
                } else {
                    author.put("authorImage", "/images/default-author.jpg"); // Imagen predeterminada
                }

                authors.add(author);
            }

            authorData.put("authors", authors);
            authorData.put("numFound", root.path("numFound").asInt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorData;
    }

    private List<Map<String, Object>> getWorksByAuthor(String authorKey, int limit) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://openlibrary.org/authors/" + authorKey + "/works.json?limit=" + limit;

        List<Map<String, Object>> works = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode entries = root.path("entries");

            for (JsonNode entry : entries) {
                Map<String, Object> work = new HashMap<>();
                work.put("title", entry.path("title").asText());
                works.add(work);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return works;
    }
    
}
