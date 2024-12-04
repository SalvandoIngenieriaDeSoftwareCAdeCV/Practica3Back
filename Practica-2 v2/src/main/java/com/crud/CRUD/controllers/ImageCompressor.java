package com.crud.CRUD.controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ImageCompressor {
    public static void main(String[] args) {

    }

    public static byte[] compressImage(byte[] inputBytes, int quality) {
       
        String ruta = Paths.get("").toAbsolutePath().toString();
        ruta = ruta+"/Practica-2 v2/src/main/java/com/crud/CRUD/controllers";
        ruta = ruta.replace("\\", "/");
        System.out.println("La ruta actual es: " + ruta);
        try {
            
            File inputFile = new File(ruta+"/input.jpeg");
            File carpeta = new File(ruta);
            Files.write(inputFile.toPath(), inputBytes);

            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "script.bat"); 
            builder.directory(carpeta);
            Process process = builder.start(); 
            int exitCode = process.waitFor();
            process.destroy();

            Path path = Paths.get(inputFile.toURI());
            inputBytes = Files.readAllBytes(path);
        } catch (IOException | InterruptedException e) {
            
            e.printStackTrace();
        }

        
        return inputBytes;
    }
}
