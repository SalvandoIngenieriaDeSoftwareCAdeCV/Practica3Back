package com.crud.CRUD.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.crud.CRUD.services.TvMazeService;

import java.util.List;
import java.util.Map;

@Controller
public class TvMazeController {

    @Autowired
    private TvMazeService tvMazeService;

    //Se tienen que ajustar los endpoints
    @GetMapping("/")
    public String home() {
        return "formulario_shows"; // Vista principal con el formulario
    }

    @PostMapping("/searchShows")
    public String searchShows(
            @RequestParam("query") String query,
            Model model) {
        // Llamamos al servicio que obtiene los shows según la búsqueda
        List<Map<String, Object>> shows = tvMazeService.searchShow(query);
        
        // Agregamos los shows al modelo con el atributo "shows"
        model.addAttribute("shows", shows);
        
        // Retornamos la vista para mostrar los resultados
        return "show_results"; // Vista para mostrar los resultados de los shows
    }
}
