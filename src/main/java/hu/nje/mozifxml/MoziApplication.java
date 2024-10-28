package hu.nje.mozifxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MoziApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Scene scene = new Scene(
                FXMLLoader.load(
                        Objects.requireNonNull(MoziApplication.class.getResource("main-view.fxml"))
                )
        );
        stage.setTitle("Mozi - Java előadás beadandó");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }
}