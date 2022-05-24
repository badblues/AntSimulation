package com.antsim.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class ConsoleController {
    @FXML
    TextArea consoleArea;
    private Stage stage;
    SimulationController simulationController;
    public void showConsole(SimulationController simulationController) {
        stage.show();
        this.simulationController = simulationController;
    }

    public void hideConsole() {
        stage.hide();
    }

    public void keyEvent(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case ESCAPE -> hideConsole();
            case ENTER -> command();
        }
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }

    public void command() {
        if (consoleArea.getText().equals("start\n")) {
            simulationController.startSimulation();
            consoleArea.setText("");
        }
        else if (consoleArea.getText().equals("stop\n")) {
            simulationController.pauseSimulation();
            consoleArea.setText("");
        }
        else
            consoleArea.setText("");
    }

}
