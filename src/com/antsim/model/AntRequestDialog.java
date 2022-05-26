package com.antsim.model;

import com.antsim.controllers.AntRequestController;
import com.antsim.controllers.ConsoleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AntRequestDialog {

    AntRequestController antRequestController;

    public AntRequestDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AntRequestDialog.fxml"));
            Parent root = loader.load();
            antRequestController = loader.getController();
            Stage stage = new Stage();
            antRequestController.initialize(stage);
            stage.setScene(new Scene(root));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public AntRequestController getAntRequestController() {
        return antRequestController;
    }

}
