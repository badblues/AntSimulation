package com.antsim;

import javafx.scene.control.Alert;

public class AlertsView {

	private static AlertsView instance;

	Alert invalidValueAlert = new Alert(Alert.AlertType.ERROR);
	Alert endSimulationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	Alert aliveAntsAlert = new Alert(Alert.AlertType.INFORMATION);

	static synchronized AlertsView getInstance() {
		if (instance == null)
			instance = new AlertsView();
		return instance;
	}

	AlertsView() {
		invalidValueAlert.setTitle("Invalid input");

		aliveAntsAlert.setTitle("Ants");

		endSimulationAlert.setTitle("Simulation info");
		endSimulationAlert.setContentText("End simulation?");
	}

	public Alert getAliveAntsAlert() {
		return aliveAntsAlert;
	}

	public Alert getEndSimulationAlert() {
		return endSimulationAlert;
	}

	public Alert getInvalidValueAlert() {
		return invalidValueAlert;
	}
}
