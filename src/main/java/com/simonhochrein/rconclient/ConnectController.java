package com.simonhochrein.rconclient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectController {
    @FXML
    public TextField addressField;

    @FXML
    public TextField passwordField;

    @FXML
    protected void onConnect() {
        try {
            var loader = RCONClientApplication.instance.switchScene("prompt.fxml");
            var controller = (PromptController) loader.getController();
            var address = addressField.getText();
            var parts = address.split(":", 2);

            if (parts.length != 2) {
                throw new IOException("Invalid address");
            }

            var host = parts[0];
            var port = Integer.parseInt(parts[1]);

            if(port < 0 || port > 65535) {
                throw new IOException("Invalid port");
            }

            controller.connect(host, port, passwordField.getText()); // protocol is prepended to make it parse properly
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            RCONClientApplication.instance.switchScene("connect.fxml");
            e.printStackTrace();
        }
    }
}