package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; // para las listas
import java.util.List;
import java.util.Scanner;

public class EmergencyManager {

    private final EmergencyDetector detector;
    private final AlertSender sender;
    private List<UserData> listaUsuarios; // aqui guardamos todos los que leamos
    private UserData usuarioActual; 

    public EmergencyManager() {
        this.detector = new EmergencyDetector();
        this.sender = new AlertSender();
        this.listaUsuarios = new ArrayList<>();

        // intentamos cargar la lista del fichero
        try {
            cargarUsuarios();
        } catch (Exception e) {
            System.out.println("Fallo al cargar la lista. Usamos el default.");
            listaUsuarios.add(new UserData("Default", "000", "Nada"));
        }
    }

    // lee el txt linea a linea separando por comas
    private void cargarUsuarios() throws IOException {
        String fichero = "user_data.txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                
                // formato: Nombre,Telefono,Info
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    // creamos el usuario y a la lista
                    UserData u = new UserData(partes[0].trim(), partes[1].trim(), partes[2].trim());
                    listaUsuarios.add(u);
                }
            }
        }
        System.out.println("Cargados " + listaUsuarios.size() + " usuarios del fichero.");
    }

    // funcion extra para elegir quien eres al principio
    private void seleccionarUsuario(Scanner scanner) {
        System.out.println("\n--- Quien eres? ---");
        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay nadie... uso el default");
            usuarioActual = new UserData("Default", "000", "Nada");
            return;
        }

        // pintamos la lista
        for (int i = 0; i < listaUsuarios.size(); i++) {
            System.out.println((i + 1) + ". " + listaUsuarios.get(i).getNombre());
        }
        
        System.out.print("Elige numero: ");
        try {
            // leemos el numero y restamos 1 porque las listas empiezan en 0
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            if (opcion > 0 && opcion <= listaUsuarios.size()) {
                usuarioActual = listaUsuarios.get(opcion - 1);
            } else {
                System.out.println("Numero incorrecto. Pillo el primero.");
                usuarioActual = listaUsuarios.get(0);
            }
        } catch (Exception e) {
            System.out.println("Eso no es un numero. Pillo el primero.");
            usuarioActual = listaUsuarios.get(0);
        }
        System.out.println("Hola " + usuarioActual.getNombre() + "!");
    }

    public void startSystem() {
        try (Scanner scanner = new Scanner(System.in)) {
            // primero elegimos el usuario
            seleccionarUsuario(scanner);

            // luego arrancamos el detector normal
            EmergencyEvent evento = detector.detectEvent(scanner, usuarioActual);

            if (evento != null) {
                sender.sendAlert(evento);
            } 
        } catch (Exception e) {
            System.out.println("Error en el sistema: " + e.getMessage());
        }
    }
}