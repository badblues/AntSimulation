package com.antsim;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Console {

	private static Console instance;

	Scene scene;
	Stage stage;

	@FXML
	TextArea consoleArea;

	static synchronized Console getInstance() {
		if(instance == null)
			instance = new Console();
		return instance;
	}

	public Console() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("view/Console.fxml"));
			scene = new Scene(root);
			stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initModality(Modality.NONE);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void showConsole() {
		stage.show();
	}

	public void hideConsole() {
		stage.hide();
	}

	public TextArea getConsoleArea() {
		return consoleArea;
	}

}
