package com.antsim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Console {

    ConsoleController consoleController;

    public Console() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Console.fxml"));
            Parent root = loader.load();
            consoleController = loader.getController();
            Stage stage = new Stage();
            consoleController.initialize(stage);
            stage.setScene(new Scene(root));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    ConsoleController getConsoleController() {
        return consoleController;
    }
}
