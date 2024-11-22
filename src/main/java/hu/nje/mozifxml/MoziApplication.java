package hu.nje.mozifxml;

import hu.nje.mozifxml.controller.MainController;
import hu.nje.mozifxml.util.Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MoziApplication extends Application {
    public static final String FXML_VIEW_NAME = "main-view.fxml";
    public static final String APP_TITLE = "app.name";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_VIEW_NAME));

        stage.setTitle(Helper.loadConfigProperties().getProperty(APP_TITLE));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setOnCloseRequest(event -> {
            final MainController controller = fxmlLoader.getController();
            controller.shutdown();
        });
        stage.show();
    }
}