package com.crud.CRUD.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLIENTE")
public class ClientModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_cliente;

    @Column
    private String nombre;

    @Column
    private String correo;

    @Column(name = "contrasena")
    private String contrasena;

    public Long getId(){
        return id_cliente;
    }

    public void setId(Long id){
        this.id_cliente = id;
    }
    
    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getCorreo(){
        return correo;
    }

    public void setCorreo(String correo){
        this.correo = correo;
    }
    
    public String getContrasena(){
        return contrasena;
    }

    public void setContrasena(String contrasena){
        this.contrasena = contrasena;
    }

}
