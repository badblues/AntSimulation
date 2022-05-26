package com.antsim.controllers;

import com.antsim.model.Habitat;
import com.antsim.view.AlertsView;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;


public class AntRequestController {
    @FXML
    TextArea clientsText;
    @FXML
    ComboBox<Integer> clientComboBox;
    @FXML
    TextField numberTextField;
    private Stage stage;
    SimulationController simulationController;

    public void showDialog(SimulationController simulationController) {
        stage.show();
        this.simulationController = simulationController;
    }

    public void hideDialog() {
        stage.hide();
    }

    public void keyEvent(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case ESCAPE -> hideDialog();
        }
    }

    public void initialize(Stage stage) {
        this.stage = stage;
    }

    public void request() {
        if (!Objects.equals(numberTextField.getText(), "") && clientComboBox.getValue() != null) {
            int id = clientComboBox.getValue();
            int number = Integer.parseInt(numberTextField.getText());
            if(number >= 1 && number <= 256) {
                ClientController.sendRequest(id, number);
            } else {
                AlertsView.getInstance().getInvalidValueAlert().setContentText("Ants number must be between 0 and 256");
                AlertsView.getInstance().getInvalidValueAlert().show();
            }
        }
    }

    public void updateClients() {
        ArrayList<Integer> array = Habitat.getInstance().getClientsArray();
        StringBuilder clients = new StringBuilder("Current client: ");
        if (!array.isEmpty()) {
            clients.append(array.get(0)).append("\n");
            for (int i = 1; i < array.size(); i++)
                clients.append("Client ").append(array.get(i)).append("\n");
        }
        clientsText.setText(clients.toString());
        synchronized (Habitat.getInstance().getClientsArray()) {
            clientComboBox.getItems().clear();
            for (int i = 1; i < array.size(); i++){
                if (!Objects.equals(array.get(i), array.get(0)))
                    clientComboBox.getItems().add(array.get(i));
            }
        }
    }
}
