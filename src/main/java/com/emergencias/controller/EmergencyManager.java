package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.data.GestorDatos;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.CentroSalud;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.Route;
import com.emergencias.model.UserData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmergencyManager {

    private final EmergencyDetector detector;
    private final AlertSender sender;
    private final GestorDatos gestorDatos; // Centralizamos la gestión de datos aquí

    private UserData usuarioActual;
    private List<CentroSalud> listaCentrosDisponibles;
    private List<Route> listaRutas; // Añadimos la lista de rutas

    public EmergencyManager() {
        this.detector = new EmergencyDetector();
        this.sender = new AlertSender();
        this.gestorDatos = new GestorDatos();
        this.listaCentrosDisponibles = new ArrayList<>();
        this.listaRutas = new ArrayList<>();

        try {
            this.usuarioActual = cargarUsuario();
        } catch (Exception e) {
            System.out.println("Fallo cargando usuario. Usando default.");
            this.usuarioActual = new UserData("Default", "000", "Nada");
        }


        cargarBasesDeDatos();

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

    private void cargarBasesDeDatos() {
        System.out.println("\nCargando bases de datos del sistema...");
        this.listaCentrosDisponibles = gestorDatos.cargarCentros();
        this.listaRutas = gestorDatos.cargarRutas(); // Cargamos las rutas usando el GestorDatos
    }

    public void startSystem() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;

            while (continuar) {
                System.out.println("\n=== 🏔️ APP DE MONTAÑA Y EMERGENCIAS ===");
                System.out.println("1. 🥾 Ver e Iniciar Rutas");
                System.out.println("2. 🆘 Detectar Emergencia (112)");
                System.out.println("3. 📋 Ver Historial de Alertas");
                System.out.println("4. ❌ Salir");
                System.out.print("Elige opcion: ");

                String entrada = scanner.nextLine().trim();

                switch (entrada) {
                    case "1":
                        gestionarRutas(scanner);
                        break;
                    case "2":
                        EmergencyEvent evento = detector.detectEvent(scanner, usuarioActual);
                        if (evento != null) sender.sendAlert(evento);
                        break;
                    case "3":
                        sender.mostrarHistorial();
                        break;
                    case "4":
                        System.out.println("Cerrando sistema... ¡Cuidado en la montaña!");
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


    private void gestionarRutas(Scanner scanner) {
        System.out.println("\n--- 🥾 RUTAS DISPONIBLES ---");
        for (int i = 0; i < listaRutas.size(); i++) {
            System.out.println((i + 1) + ". " + listaRutas.get(i).toString());
        }
        System.out.println("0. Volver atrás");
        System.out.print("Elige una ruta para iniciar/puntuar (0-" + listaRutas.size() + "): ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            if (opcion > 0 && opcion <= listaRutas.size()) {
                Route rutaSeleccionada = listaRutas.get(opcion - 1);
                System.out.println("\nHas finalizado la ruta: " + rutaSeleccionada.getNombre());
                System.out.print("Del 1 al 5, ¿qué puntuación le das?: ");

                double nota = Double.parseDouble(scanner.nextLine().replace(",", "."));
                rutaSeleccionada.addPuntuacion(nota);


                gestorDatos.guardarRutas(listaRutas);

                System.out.println("¡Gracias! La nota media ahora es: " + String.format("%.1f", rutaSeleccionada.getNotaMedia()));
            } else if (opcion != 0) {
                System.out.println("⚠️ Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Error: Debes introducir un número.");
        }
    }
}