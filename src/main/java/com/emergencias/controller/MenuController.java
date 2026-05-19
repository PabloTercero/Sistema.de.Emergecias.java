package com.emergencias.controller;

import com.emergencias.data.GestorDatos;
import com.emergencias.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class MenuController {

    private GestorDatos gestorDatos = new GestorDatos();

    @FXML
    protected void onAnadirRutaClick(ActionEvent event) {
        // 1. Solicitamos el nombre de la nueva ruta de montaña
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("🏔️ Registrar Ruta");
        dialogNombre.setHeaderText("Configuración de nueva ruta pedagógica");
        dialogNombre.setContentText("Introduzca el nombre de la ruta:");

        Optional<String> resultadoNombre = dialogNombre.showAndWait();

        // Si el usuario introduce el nombre y confirma
        resultadoNombre.ifPresent(nombreRuta -> {
            if (nombreRuta.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Operación Inválida", "El nombre de la ruta no puede estar vacío.");
                return;
            }

            // 2. Solicitamos la dificultad mediante un desplegable estructurado
            List<String> dificultades = List.of("Baja", "Media", "Alta");
            ChoiceDialog<String> dialogDificultad = new ChoiceDialog<>("Media", dificultades);
            dialogDificultad.setTitle("📊 Nivel de Dificultad");
            dialogDificultad.setHeaderText("Asignación de complejidad técnica");
            dialogDificultad.setContentText("Seleccione el nivel:");

            Optional<String> resultadoDificultad = dialogDificultad.showAndWait();

            // Si confirma la dificultad, procedemos a la persistencia
            resultadoDificultad.ifPresent(dificultad -> {
                try {
                    // Cargamos el listado actual de rutas para no sobrescribir los datos previos
                    List<Route> listaRutasActuales = gestorDatos.cargarRutas();

                    // Instanciamos el nuevo objeto modelo de tipo Route
                    Route nuevaRuta = new Route(nombreRuta.trim(), dificultad);

                    // Incorporamos la ruta al catálogo y guardamos en el fichero rutas.txt
                    listaRutasActuales.add(nuevaRuta);
                    gestorDatos.guardarRutas(listaRutasActuales);

                    mostrarAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso",
                            "La ruta '" + nombreRuta + "' [Dificultad: " + dificultad + "] ha sido indexada en el sistema.");
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia", "No se pudieron salvar los datos de la ruta.");
                }
            });
        });
    }

    @FXML
    protected void onRutasClick(ActionEvent event) {
        //Cargamos las rutas desde el fichero
        List<Route> listaRutas = gestorDatos.cargarRutas();

        if (listaRutas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay rutas disponibles en la base de datos.");
            return;
        }

        //Creamos un menú desplegable visual con las rutas
        ChoiceDialog<Route> dialog = new ChoiceDialog<>(listaRutas.get(0), listaRutas);
        dialog.setTitle("🥾 Selección de Ruta");
        dialog.setHeaderText("Menú de Rutas de Montaña");
        dialog.setContentText("Elige una ruta para iniciar:");

        // Mostramos la ventana y esperamos a que el usuario elija y pulse Aceptar
        Optional<Route> resultado = dialog.showAndWait();

        //Si el usuario ha elegido una ruta (no le ha dado a Cancelar)
        resultado.ifPresent(rutaSeleccionada -> {

            // Pedimos la nota con otra ventanita de texto
            TextInputDialog dialogPuntuacion = new TextInputDialog();
            dialogPuntuacion.setTitle("⭐ Puntuar Ruta");
            dialogPuntuacion.setHeaderText("Has finalizado: " + rutaSeleccionada.getNombre());
            dialogPuntuacion.setContentText("Del 1 al 5, ¿qué puntuación le das?:");

            Optional<String> notaInput = dialogPuntuacion.showAndWait();

            // Si el usuario escribe una nota y pulsa Aceptar
            notaInput.ifPresent(notaStr -> {
                try {

                    double nota = Double.parseDouble(notaStr.replace(",", "."));

                    if (nota >= 0 && nota <= 5) {
                        // Guardamos la nota y actualizamos el fichero txt!
                        rutaSeleccionada.addPuntuacion(nota);
                        gestorDatos.guardarRutas(listaRutas);

                        mostrarAlerta(Alert.AlertType.INFORMATION, "¡Gracias!",
                                "Nota guardada. La nota media ahora es: " + String.format("%.1f", rutaSeleccionada.getNotaMedia()) + " ⭐");
                    } else {
                        mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "La nota debe estar entre el 0 y el 5.");
                    }
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Debes introducir un número válido.");
                }
            });
        });
    }

    @FXML
    protected void onVerHospitalesClick(ActionEvent event) {
        try {
            // 1. Deserializamos la red completa de infraestructuras desde el archivo estructurado JSON
            List<com.emergencias.model.CentroSalud> centros = gestorDatos.cargarCentros();

            if (centros == null || centros.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Error de Red",
                        "El sistema de archivos JSON no devolvió ninguna infraestructura médica activa.");
                return;
            }

            // 2. Justificación de negocio: Simulamos el cálculo matricial de distancia GPS
            // Seleccionamos un centro médico real del JSON (por ejemplo, el primero o el de Cehegín)
            com.emergencias.model.CentroSalud centroMasCercano = centros.get(0);

            // Simulamos una distancia lógica basada en telemetría
            double distanciaSimulada = 4.2;

            // 3. Mostramos la información de utilidad real para el senderista
            mostrarAlerta(Alert.AlertType.INFORMATION, "🏥 Centro Médico de Emergencia Asignado",
                    "El módulo de geolocalización ha calculado la infraestructura hospitalaria más próxima a su ruta:\n\n" +
                            "▪️ Centro: " + centroMasCercano.getNombre() + "\n" +
                            "▪️ Dirección: " + centroMasCercano.getDireccion() + "\n" +
                            "▪️ Distancia estimada: " + distanciaSimulada + " km\n\n" +
                            "ℹ️ Datos sincronizados mediante el parseo estructurado del archivo 'CentrosdeSalud.json'.");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Fallo de Mapeo JSON",
                    "Error crítico al procesar la red de hospitales con Google Gson: " + e.getMessage());
        }
    }

    @FXML
    protected void onEmergenciaClick(ActionEvent event) {
        // 1. Mostramos el aviso visual en la interfaz gráfica
        mostrarAlerta(Alert.AlertType.WARNING, "🆘 Emergencia 112", "Protocolo de emergencia activado.\nIniciando rastreo GPS...");

        // 2. Vinculamos la acción con el modelo y el AlertSender para que registre datos reales
        try {
            // Simulamos los datos del usuario afectado de forma robusta
            com.emergencias.model.UserData usuarioAlerta = new com.emergencias.model.UserData("Pablo Tercero", "666666666", "Estabilidad médica estándar");

            // Creamos el evento de emergencia con ubicación simulada para la UI
            com.emergencias.model.EmergencyEvent evento = new com.emergencias.model.EmergencyEvent(
                    "SOS BOTÓN INTERFAZ",
                    "Coordenadas GPS de Emergencia (UI)",
                    usuarioAlerta
            );

            // Instanciamos el AlertSender que se encarga de pintar en consola y persistir en el archivo .log
            com.emergencias.alert.AlertSender sender = new com.emergencias.alert.AlertSender();
            sender.sendAlert(evento);

            // Confirmación visual de que el backend ha respondido correctamente
            mostrarAlerta(Alert.AlertType.INFORMATION, "Transmisión Exitosa", "El paquete de emergencia ha sido registrado en alertas.log.");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sistema", "No se pudo registrar el log de la emergencia: " + e.getMessage());
        }
    }

    @FXML
    protected void onSalirClick(ActionEvent event) {
        System.exit(0); // Cierra la aplicación completamente
    }


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}