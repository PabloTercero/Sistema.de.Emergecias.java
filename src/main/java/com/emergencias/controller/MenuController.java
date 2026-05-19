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
    protected void onEmergenciaClick(ActionEvent event) {
        mostrarAlerta(Alert.AlertType.WARNING, "🆘 Emergencia 112", "Protocolo de emergencia activado.\nIniciando rastreo GPS...");
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