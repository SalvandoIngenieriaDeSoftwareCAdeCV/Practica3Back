package com.crud.CRUD.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.crud.CRUD.services.OpenLibraryService;

import java.util.List;
import java.util.Map;

@Controller
public class OpenLibraryController {

    @Autowired
    private OpenLibraryService openLibraryService;

    //Se tienen que ajustar los endpoints
    @GetMapping("/")
    public String home() {
        return "formulario_libros"; // Vista principal con el formulario
    }

    @PostMapping("/search")
    public String searchBooks(
            @RequestParam("query") String query,
            @RequestParam("type") String type,
            Model model) {

        if (type.equals("author")) {
            Map<String, Object> authorResults = openLibraryService.searchAuthors(query);
            model.addAttribute("authorResults", authorResults);
            return "author_results"; // Vista para mostrar la búsqueda con el filtro de autor.
        } else {
            List<Map<String, Object>> books = openLibraryService.searchBooks(query, type);
            model.addAttribute("books", books);
            return "book_results"; // Vista para mostrar la búsqueda con el filtro de título.
        }
    }
}
