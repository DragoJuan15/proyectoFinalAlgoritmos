package org.example.proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    // esta sera mi gran despedida, ya que aqui esta mi gran amor a mi carrera, diria T O D O pero
    // si lo escribo se pone verde el codigo, no se porque, pero dejeme decirle que amo el estudio
    // y por nada del mundo reprobare
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Gestión de Grafos Dirigidos");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
// creado por Juan Sarmiento Ceceña (JSC) y Axel Eduardo Jimenez Perez, tambien llamados JUAXEL
