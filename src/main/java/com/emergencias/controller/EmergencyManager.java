package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EmergencyManager {

    private final EmergencyDetector detector;
    private final AlertSender sender;
    private UserData usuarioActual;

    public EmergencyManager() {
        this.detector = new EmergencyDetector();
        this.sender = new AlertSender();

        try {
            this.usuarioActual = cargarUsuario();
        } catch (Exception e) {
            System.out.println("Fallo cargando usuario. Usando default.");
            this.usuarioActual = new UserData("Default", "000", "Nada");
        }
        System.out.println("Sistema listo. Usuario: " + usuarioActual.getNombre());
    }

    private UserData cargarUsuario() throws IOException {
        String fichero = "user_data.txt";
        Map<String, String> datos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    datos.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        return new UserData(datos.get("nombre"), datos.get("telefono"), datos.get("infoMedica"));
    }

    public void startSystem() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;

            // FIX: BUCLE WHILE PARA MANTENER EL SISTEMA VIVO
            while (continuar) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Detectar Emergencia");
                System.out.println("2. Ver Historial");
                System.out.println("3. Salir");
                System.out.print("Elige opcion: ");

                String entrada = scanner.nextLine().trim();

                switch (entrada) {
                    case "1":
                        EmergencyEvent evento = detector.detectEvent(scanner, usuarioActual);
                        if (evento != null) sender.sendAlert(evento);
                        break;
                    case "2":
                        sender.mostrarHistorial();
                        break;
                    case "3":
                        System.out.println("Cerrando sistema... Adios!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opcion no valida, prueba otra vez.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error critico: " + e.getMessage());
        }
    }
}