package com.crud.CRUD.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.CRUD.models.ClientModel;
import com.crud.CRUD.services.ClientServices;

@RestController
@RequestMapping("/cliente")
public class ClientController {
    
    @Autowired
    private ClientServices userService;

    @PostMapping("/saveUser")
    public ClientModel saveUser(@RequestBody ClientModel user){
        return this.userService.saveUser(user);
    }

    @GetMapping("/getUser/{id}")
    public Optional<ClientModel> getUserById(@PathVariable Long id){
        return this.userService.getById(id); 
    }

    @PutMapping("/updateUserByEmail")
    public ClientModel updateUserByEmail(@RequestBody ClientModel request) {
        return this.userService.updateByEmail(request);
    }

    @DeleteMapping("/deleteUserByEmail")
    public ResponseEntity<?> deleteUserByEmail(@RequestBody Map<String, String> payload) {
        String correo = payload.get("correo");
        Optional<ClientModel> user = userService.findByEmail(correo);

        if (user.isPresent()) {
            userService.deleteUserByEmail(correo);
            return ResponseEntity.ok().body("Usuario eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody ClientModel request) {
        Map<String, Object> response = new HashMap<>();
        Optional<ClientModel> user = userService.findByEmailAndPassword(request.getCorreo(), request.getContrasena());
        if (user.isPresent()) {
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("userId", user.get().getId());
            response.put("rol", user.get().getRol()); // Enviar el rol al frontend
        } else {
            response.put("success", false);
            response.put("message", "Correo o contraseña incorrectos");
        }
        return response;
    }

    // Nuevo endpoint para obtener los datos del usuario autenticado
    @GetMapping("/getUserData/{id}")
    public Optional<ClientModel> getUserData(@PathVariable Long id) {
        return userService.getById(id);
    }

    // Nuevo endpoint para obtener todos los usuarios (solo para el administrador)
    @GetMapping("/getAllUsers")
    public List<ClientModel> getAllUsers() {
        return userService.getUsers();
    }


    // Endpoint de prueba para obtener información de un usuario por su id
    @GetMapping("/public/getUserData/{id}")
    public Optional<ClientModel> getUserDataPublic(@PathVariable Long id) {
        return userService.getById(id);
    }
}