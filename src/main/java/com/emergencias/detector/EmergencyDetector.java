package com.emergencias.detector;

import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;
import java.util.Scanner;
import java.util.Random;

public class EmergencyDetector {

    // Umbral de activación automática parametrizado para simulación de impacto
    private static final int UMBRAL_CRITICO = 5;

    public EmergencyEvent detectEvent(Scanner scanner, UserData usuario) {
        System.out.println("\n--- Módulo Detector de Contingencias ---");
        System.out.println("Seleccione modalidad operativa: 'E' (Manual) o 'A' (Automatizada)");
        String opcion = scanner.nextLine().trim();

        String tipo = "No definido";
        String ubicacion = "Sin registros de telemetría";

        if (opcion.equalsIgnoreCase("E")) {
            // Entrada manual de datos por parte del operador del sistema
            System.out.print("Introduzca tipo de contingencia: ");
            tipo = scanner.nextLine().trim();
            System.out.print("Introduzca coordenadas o ubicación actual: ");
            ubicacion = scanner.nextLine().trim();

        } else if (opcion.equalsIgnoreCase("A")) {
            // Simulación de lectura mediante sensor acelerómetro integrado en terminal móvil
            int fuerzaImpacto = new Random().nextInt(10) + 1;
            if (fuerzaImpacto >= UMBRAL_CRITICO) {
                tipo = "ALERTA AUTOMÁTICA (ACELERÓMETRO)";
                ubicacion = "Coordenadas GPS estimadas por dispositivo";
                System.out.println("Impacto crítico detectado. Magnitud registrada: " + fuerzaImpacto);
            } else {
                System.out.println("Variación de fuerza leve (" + fuerzaImpacto + "). Descartando activación del protocolo.");
                return null;
            }
        } else {
            System.out.println("Opción del sistema no válida.");
            return null;
        }

        // Requerimiento de confirmación activa por motivos de seguridad operacional
        System.out.print("¿Confirmar transmisión del paquete de rescate al 112? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            return new EmergencyEvent(tipo, ubicacion, usuario);
        }

        System.out.println("Operación abortada por el usuario.");
        return null;
    }
}