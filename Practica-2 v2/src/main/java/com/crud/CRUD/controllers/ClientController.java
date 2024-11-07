package com.crud.CRUD.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; // Asegúrate de que esta sea la única importación de Map
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getUsers")
    public ArrayList<ClientModel> getUsers(){
        return this.userService.getUsers();
    }

    @PostMapping("/saveUser")
    public ClientModel saveUser(@RequestBody ClientModel user){
        return this.userService.saveUser(user);
    }

    @GetMapping("/getUser/{id}")
    public Optional<ClientModel> getUserById(@PathVariable Long id){
        return this.userService.getById(id); 
    }

    @PutMapping("/updateUser/{id}")
    public ClientModel updateUserById(@RequestBody ClientModel request, @PathVariable Long id){
        return this.userService.updateById(request, id);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUserById(@PathVariable("id") Long id){
        boolean ok = this.userService.deleteUserById(id);
        if(ok){
            return "Usuario eliminado correctamente";
        }else{
            return "El usuario no pudo ser eliminado";
        }
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody ClientModel request) {
        Map<String, Object> response = new HashMap<>();
        Optional<ClientModel> user = userService.findByEmailAndPassword(request.getCorreo(), request.getContrasena());
        if (user.isPresent()) {
            response.put("success", true);
            response.put("message", "Login exitoso");
            // Opcional: puedes incluir un token simple o identificador de sesión
            response.put("userId", user.get().getId()); // o cualquier identificador único
        } else {
            response.put("success", false);
            response.put("message", "Correo o contraseña incorrectos");
        }
        return response;
    }
}