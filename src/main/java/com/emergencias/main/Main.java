package com.emergencias;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargamos el diseño de SceneBuilder
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/MenuPrincipal.fxml"));

        // ajustamos el tamaño de 400x400
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        // Configuramos la ventana
        stage.setTitle("🏔️ Sistema de Emergencias");
        stage.setScene(scene);
        stage.show(); // ¡Mostramos la ventana!
    }

    public static void main(String[] args) {
        // En vez de lanzar la consola, lanzamos la app gráfica
        launch(args);
    }
}