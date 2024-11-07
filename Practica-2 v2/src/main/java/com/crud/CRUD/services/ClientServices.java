
package com.crud.CRUD.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.CRUD.models.ClientModel;
import com.crud.CRUD.repositories.IClientRepository;

@Service
public class ClientServices {
    
    @Autowired
    IClientRepository userRepository;

    public ArrayList<ClientModel> getUsers(){
        return (ArrayList<ClientModel>) userRepository.findAll();
    }

    public ClientModel saveUser(ClientModel user){
        return userRepository.save(user);
    }

    public Optional<ClientModel> getById(Long id){
        return userRepository.findById(id);
    }

    public ClientModel updateById(ClientModel request, Long id){
        ClientModel user = userRepository.findById(id).get();
        user.setNombre(request.getNombre());
        user.setCorreo(request.getCorreo());
        user.setContrasena(request.getContrasena());
        userRepository.save(user);

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
        return userRepository.findByCorreoAndContrasena(correo, contrasena);
    }    
}