package com.crud.CRUD.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class TvMazeService {

    public List<Map<String, Object>> searchShow(String query) {
        RestTemplate restTemplate = new RestTemplate();
        
        // Codificamos el query para que sea compatible con la URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        
        // Construimos la URL con el query codificado
        String url = "https://api.tvmaze.com/search/shows?q=" + encodedQuery;

        List<Map<String, Object>> shows = new ArrayList<>();
        try {
            // Realizamos la solicitud GET a la API
            String response = restTemplate.getForObject(url, String.class);

            // Procesamos la respuesta con ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode showsNode = root;

            for (JsonNode show : showsNode) {
                Map<String, Object> showData = new HashMap<>();
                showData.put("title", show.path("show").path("name").asText());
                showData.put("summary", show.path("show").path("summary").asText());
                showData.put("rating", show.path("show").path("rating").path("average").asDouble());
                showData.put("status", show.path("show").path("status"));

                // Si la imagen del show está disponible, la agregamos
                JsonNode imageNode = show.path("show").path("image");
                if (!imageNode.isMissingNode()) {
                    String imageUrl = imageNode.path("medium").asText();
                    showData.put("coverImage", imageUrl);
                } else {
                    showData.put("coverImage", "/images/default-show-cover.jpg"); // Imagen por defecto si no hay
                }

                shows.add(showData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shows;
    }
}
