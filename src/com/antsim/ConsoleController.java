package com.antsim;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleController {
    @FXML
    TextArea consoleArea;
    private Stage stage;
    public void showConsole() {
        stage.show();

    }

    public void hideConsole() {
        stage.hide();
    }

    public void keyEvent(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case ESCAPE -> hideConsole();
        }
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }
}
