package com.simonhochrein.rconclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RCONClientApplication extends Application {
    public static RCONClientApplication instance;
    public Stage stage;

    public RCONClientApplication() {
        instance = this; // I hate this, but it seems like the only way to get application context in javafx
    }

    Throwable getCause(Throwable e) {
        Throwable cause;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        switchScene("connect.fxml");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            new Alert(Alert.AlertType.ERROR, getCause(e).getMessage()).showAndWait();
            e.printStackTrace();
            RCONClientApplication.instance.switchScene("connect.fxml");
        });
    }

    public FXMLLoader switchScene(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RCONClientApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(RCONClientApplication.class.getResource("main.css")).toExternalForm());
            stage.setTitle("RCON Client");
            stage.setScene(scene);
            stage.show();
            stage.sizeToScene();

            return fxmlLoader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch();
    }
}