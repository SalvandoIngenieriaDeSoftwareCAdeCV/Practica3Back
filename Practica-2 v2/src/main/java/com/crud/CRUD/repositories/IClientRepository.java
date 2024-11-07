package com.crud.CRUD.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.CRUD.models.ClientModel;

@Repository
public interface IClientRepository extends JpaRepository<ClientModel, Long> {
    
    // Método para encontrar un usuario por correo y contraseña
    Optional<ClientModel> findByCorreoAndContrasena(String correo, String contrasena);
}