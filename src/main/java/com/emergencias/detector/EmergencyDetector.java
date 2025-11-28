package com.emergencias.detector;

import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;
import java.util.Scanner;
import java.util.Random; 

public class EmergencyDetector {

    // valor minimo para que salte la alarma automatica
    private static final int UMBRAL = 5; 

    public EmergencyEvent detectEvent(Scanner scanner, UserData usuario) {
        System.out.println("\n--- Detector de Emergencias ---");
        System.out.println("Elige modo: 'E' (Manual) o 'A' (Automatico)");
        String opcion = scanner.nextLine().trim();

        String tipo = "Desconocido";
        String ubicacion = "Sin datos"; 

        if (opcion.equalsIgnoreCase("E")) {
            // modo manual, pedimos datos
            System.out.print("Dime el tipo de emergencia: ");
            tipo = scanner.nextLine().trim();
            System.out.print("Dime la ubicacion: ");
            ubicacion = scanner.nextLine().trim();

        } else if (opcion.equalsIgnoreCase("A")) {
            // simulamos un sensor con un random
            int fuerza = new Random().nextInt(10) + 1; 
            
            if (fuerza >= UMBRAL) {
                tipo = "ACCIDENTE AUTOMATICO";
                ubicacion = "Ubicacion GPS Simulada";
                System.out.println("GOLPE DETECTADO! Fuerza: " + fuerza);
            } else {
                System.out.println("Golpe suave (" + fuerza + "). No activamos nada.");
                return null; 
            }
        } else {
            System.out.println("Opcion no valida.");
            return null;
        }

        // confirmacion final por seguridad
        System.out.print("Confirmar envio de ayuda? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            return new EmergencyEvent(tipo, ubicacion, usuario);
        }
        
        System.out.println("Cancelado por el usuario.");
        return null;
    }
}