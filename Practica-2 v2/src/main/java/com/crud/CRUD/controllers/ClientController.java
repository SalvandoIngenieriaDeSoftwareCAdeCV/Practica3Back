package com.crud.CRUD.controllers;

import java.io.IOException;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crud.CRUD.models.ClientModel;
import com.crud.CRUD.services.ClientServices;

@RestController
@RequestMapping("/cliente")
public class ClientController {
    
    @Autowired
    private ClientServices userService;

    @PostMapping("/saveUser")
    public ClientModel saveuser(
        @RequestParam("nombre") String nombre,
        @RequestParam("apellidoPaterno") String apellidoPaterno,
        @RequestParam("apellidoMaterno") String apellidoMaterno,
        @RequestParam("correo") String correo,
        @RequestParam("contrasena") String contrasena,
        @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ){
        ClientModel user = new ClientModel();
        try{
            if(imagen != null){
                byte[] imageBytes = imagen.getBytes();
                byte[] nimagen;
                imageBytes = ImageCompressor.compressImage(imageBytes, 75);
                nimagen = Arrays.copyOf(imageBytes, imageBytes.length);
                user.setImagen(nimagen);
            }
            user.setNombre(nombre);
            user.setApellidoPaterno(apellidoPaterno);
            user.setApellidoMaterno(apellidoMaterno);
            user.setCorreo(correo);
            user.setContrasena(contrasena);
            
        }catch (IOException e){
            e.printStackTrace();
        }
        return this.userService.saveUser(user);
    }

    @GetMapping("/getUser/{id}")
    public Optional<ClientModel> getUserById(@PathVariable Long id){
        Optional<ClientModel> temp = this.userService.getById(id);
        return temp;
    }

    @GetMapping("/getUser/{correo}")
    public Optional<ClientModel> getUserByEmail(@PathVariable String correo) {
        Optional<ClientModel> temp = this.userService.getByEmail(correo);
        return temp;
    }
    

    @PutMapping("/updateUserByEmail")
    public ClientModel updateUserByEmail(
        @RequestParam("correo") String correo,
        @RequestParam(value = "imagen", required = false) MultipartFile imagen,
        @RequestParam(value = "nombre", required = false) String nombre,
        @RequestParam(value = "apellidoPaterno", required = false) String apellidoPaterno,
        @RequestParam(value = "apellidoMaterno", required = false) String apellidoMaterno,
        @RequestParam(value = "contrasena", required = false) String contrasena,
        @RequestParam(value = "correon", required = false) String correon,
        @RequestParam(value = "rol", required = false) Integer rol,
        @RequestHeader("modo") int modo,
        @RequestHeader("mod") int mod
    ) {
        ClientModel user = new ClientModel();
        try{
            Optional<ClientModel> temp = this.userService.findByEmail(correo);
            user = temp.get();
            if(imagen != null){
                byte[] imageBytes = imagen.getBytes();
                byte[] nimagen;
                imageBytes = ImageCompressor.compressImage(imageBytes, 75);
                nimagen = Arrays.copyOf(imageBytes, imageBytes.length);
                user.setImagen(nimagen);
            }
            if(nombre != null)
            user.setNombre(nombre);
            if(apellidoPaterno != null)
            user.setApellidoPaterno(apellidoPaterno);
            if(apellidoMaterno != null)
            user.setApellidoMaterno(apellidoMaterno);
            if(contrasena != null)
            user.setContrasena(contrasena);
            if(rol != null)
                user.setRol(rol);
            user.setCorreo(correo);
        }catch (IOException e){
            e.printStackTrace();
        }
        return this.userService.updateByEmail(user, modo, correon, mod);
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