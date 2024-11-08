package com.crud.CRUD.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.CRUD.models.ClientModel;
import com.crud.CRUD.repositories.IClientRepository;

@Service
public class ClientServices {

    @Autowired
    IClientRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ArrayList<ClientModel> getUsers(){
        return (ArrayList<ClientModel>) userRepository.findAll();
    }

    public ClientModel saveUser(ClientModel user){
        // Establecer rol predeterminado si no se especifica
        if (user.getRol() == null) {
            user.setRol(1);
        }
        // Encriptar la contraseña antes de guardarla
        user.setContrasena(passwordEncoder.encode(user.getContrasena()));
        return userRepository.save(user);
    }

    public Optional<ClientModel> getById(Long id){
        return userRepository.findById(id);
    }

    public ClientModel updateById(ClientModel request, Long id){
        ClientModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setNombre(request.getNombre());
            user.setCorreo(request.getCorreo());
            // Encriptar la contraseña antes de actualizar
            user.setContrasena(passwordEncoder.encode(request.getContrasena()));
            userRepository.save(user);
        }
        return user;
    }

    public Boolean deleteUserById(Long id){
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<ClientModel> findByEmailAndPassword(String correo, String contrasena) {
        Optional<ClientModel> user = userRepository.findByCorreo(correo);
        if (user.isPresent() && passwordEncoder.matches(contrasena, user.get().getContrasena())) {
            return user;
        }
        return Optional.empty();
    }    
}