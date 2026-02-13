package com.emergencias.alert;

import com.emergencias.model.EmergencyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class AlertSender {

    private static final String LOG_FILE = "alertas.log";

    public void sendAlert(EmergencyEvent event) {
        if (event == null) return;

        System.out.println("\n--- Enviando Alerta ---");
        System.out.println("Conectando con 112...");
        System.out.println("DATOS ENVIADOS: " + event.toString());

        guardarEnLog(event);
    }

    private void guardarEnLog(EmergencyEvent event) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(event.toString());
            System.out.println("(Guardado en " + LOG_FILE + ")");

        } catch (IOException e) {
            System.err.println("Fallo al escribir en el log: " + e.getMessage());
        }
    }


    public void mostrarHistorial() {
        System.out.println("\n--- Historial de Alertas ---");
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String linea;
            boolean vacio = true;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
                vacio = false;
            }
            if (vacio) System.out.println("No hay alertas registradas aun.");

        } catch (IOException e) {
            System.out.println("No se pudo leer el historial (quizas no existe aun).");
        }
    }
}