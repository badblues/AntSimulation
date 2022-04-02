package com.antsim;

import javafx.application.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

//TODO add additional view classes

public class View {

	private static View instance;
	private final Group root = new Group();
	private final Scene scene = new Scene(root, 940, 540);
	private final Text timeText = new Text("Simulation time:   ");
	private final Button startButton = new Button("Start");
	private final Button pauseButton = new Button("Pause");
	private final Button showAliveAntsButton = new Button("Show alive ants");
	private final Alert aliveAntsAlert = new Alert(Alert.AlertType.INFORMATION);
	private final Alert endSimulationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	private final Alert invalidValueAlert = new Alert(Alert.AlertType.ERROR);
	private final ToggleGroup timeButtonsGroup = new ToggleGroup();
	private final RadioButton showTimeButton = new RadioButton("Show time");
	private final RadioButton hideTimeButton = new RadioButton("Hide time");
	private final CheckBox showInfoButton = new CheckBox("Show info");
	private final ComboBox<Integer> antWarriorSpawnChanceBox = new ComboBox<>();
	private final ComboBox<Integer> antWorkerSpawnChanceBox = new ComboBox<>();
	private final Slider antWarriorSpawnTimeSlider = new Slider();
	private final Slider antWorkerSpawnTimeSlider = new Slider();
	private final TextField antWarriorSpawnTimeTextF = new TextField();
	private final TextField antWorkerSpawnTimeTextF = new TextField();
	private final TextField antWarriorLifeTimeTextF = new TextField();
	private final TextField antWorkerLifeTimeTextF = new TextField();
	private final MenuBar menuBar = new MenuBar();
	private final Menu simulationMenu = new Menu("Simulation");
	private final MenuItem startMenuItem = new MenuItem("Start");
	private final MenuItem endMenuItem = new MenuItem("End");
	private final MenuItem pauseMenuItem = new MenuItem("Pause");
	private Stage stage;

	View() {}

	static synchronized View getInstance() {
		if(instance == null)
			instance = new View();
		return instance;
	}

	void init(Stage stage) {
		this.stage = stage;
		backgroundSetUp();
		stageSetUp();
		timeTextSetUp();
		buttonsSetUp();
		menuSetUp();
		textSetUp();
		alertsSetUp();
		stage.show();
	}

	Group getRoot() {
		return root;
	}

	Scene getScene() {
		return scene;
	}

	Text getTimeText() {
		return timeText;
	}

	Button getStartButton() {
		return startButton;
	}

	Button getPauseButton() {
		return pauseButton;
	}

	Button getShowAliveAntsButton() {
		return showAliveAntsButton;
	}

	Alert getAliveAntsAlert() {
		return aliveAntsAlert;
	}

	Alert getEndSimulationAlert() {
		return endSimulationAlert;
	}

	Alert getInvalidValueAlert() {return invalidValueAlert;}

	RadioButton getShowTimeButton() {
		return showTimeButton;
	}

	RadioButton getHideTimeButton() {
		return hideTimeButton;
	}

	CheckBox getShowInfoButton() {
		return showInfoButton;
	}

	ComboBox<Integer> getAntWarriorSpawnChanceBox() {
		return antWarriorSpawnChanceBox;
	}

	ComboBox<Integer> getAntWorkerSpawnChanceBox() {
		return antWorkerSpawnChanceBox;
	}

	Slider getAntWarriorSpawnTimeSlider() {
		return antWarriorSpawnTimeSlider;
	}

	Slider getAntWorkerSpawnTimeSlider() {
		return antWorkerSpawnTimeSlider;
	}

	TextField getAntWarriorSpawnTimeTextF() {
		return antWarriorSpawnTimeTextF;
	}

	TextField getAntWorkerSpawnTimeTextF() {
		return antWorkerSpawnTimeTextF;
	}

	TextField getAntWarriorLifeTimeTextF() {
		return antWarriorLifeTimeTextF;
	}

	TextField getAntWorkerLifeTimeTextF() {
		return antWorkerLifeTimeTextF;
	}

	MenuItem getStartMenuItem() {
		return startMenuItem;
	}

	MenuItem getEndMenuItem() {
		return endMenuItem;
	}

	MenuItem getPauseMenuItem() {
		return pauseMenuItem;
	}

	private void backgroundSetUp() {
		scene.setFill(Color.GREY);
		Rectangle area = new Rectangle(200, 0, 540, 540);
		area.setFill(new Color(0.63, 0.39, 0.24, 1));
		root.getChildren().add(area);
	}

	private void stageSetUp() {

		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("AntSimulation");
		Image icon = new Image("resources/icon_ant.jpg");
		stage.getIcons().add(icon);
		stage.setOnCloseRequest(windowEvent -> {
			Platform.exit();
			System.exit(0);
		});
	}

	private void timeTextSetUp() {
		timeText.setX(370);
		timeText.setY(45);
		timeText.setFont(Font.font("Verdana"));
		timeText.setFill(Color.WHITE);
		timeText.setVisible(false);
		root.getChildren().add(timeText);
	}

	private void buttonsSetUp() {
		startButton.setLayoutX(50);
		startButton.setLayoutY(50);
		root.getChildren().add(startButton);

		pauseButton.setLayoutX(100);
		pauseButton.setLayoutY(50);
		pauseButton.setDisable(true);
		root.getChildren().add(pauseButton);

		showAliveAntsButton.setLayoutX(40);
		showAliveAntsButton.setLayoutY(180);
		root.getChildren().add(showAliveAntsButton);

		showTimeButton.setLayoutX(30);
		showTimeButton.setLayoutY(100);
		showTimeButton.setToggleGroup(timeButtonsGroup);
		root.getChildren().add(showTimeButton);

		hideTimeButton.setLayoutX(30);
		hideTimeButton.setLayoutY(120);
		hideTimeButton.setToggleGroup(timeButtonsGroup);
		hideTimeButton.setSelected(true);
		root.getChildren().add(hideTimeButton);

		showInfoButton.setLayoutX(30);
		showInfoButton.setLayoutY(140);
		showInfoButton.setSelected(false);
		root.getChildren().add(showInfoButton);

		ObservableList<Integer> chances = FXCollections.observableArrayList(
				0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);

		antWarriorSpawnChanceBox.setItems(chances);
		antWarriorSpawnChanceBox.setLayoutX(800);
		antWarriorSpawnChanceBox.setLayoutY(60);
		antWarriorSpawnChanceBox.setValue(100);
		root.getChildren().add(antWarriorSpawnChanceBox);

		antWorkerSpawnChanceBox.setItems(chances);
		antWorkerSpawnChanceBox.setLayoutX(800);
		antWorkerSpawnChanceBox.setLayoutY(110);
		antWorkerSpawnChanceBox.setValue(100);
		root.getChildren().add(antWorkerSpawnChanceBox);

		antWarriorSpawnTimeSlider.setLayoutX(750);
		antWarriorSpawnTimeSlider.setLayoutY(170);
		antWarriorSpawnTimeSlider.setShowTickMarks(true);
		antWarriorSpawnTimeSlider.setMinorTickCount(0);
		antWarriorSpawnTimeSlider.setMajorTickUnit(1);
		antWarriorSpawnTimeSlider.setSnapToTicks(true);
		antWarriorSpawnTimeSlider.setMin(Habitat.MIN_SPAWN_DELAY);
		antWarriorSpawnTimeSlider.setMax(Habitat.MAX_SPAWN_DELAY);
		antWarriorSpawnTimeSlider.setValue(1);
		root.getChildren().add(antWarriorSpawnTimeSlider);

		antWorkerSpawnTimeSlider.setLayoutX(750);
		antWorkerSpawnTimeSlider.setLayoutY(220);
		antWorkerSpawnTimeSlider.setShowTickMarks(true);
		antWorkerSpawnTimeSlider.setMajorTickUnit(1);
		antWorkerSpawnTimeSlider.setMinorTickCount(0);
		antWorkerSpawnTimeSlider.setSnapToTicks(true);
		antWorkerSpawnTimeSlider.setMin(Habitat.MIN_SPAWN_DELAY);
		antWorkerSpawnTimeSlider.setMax(Habitat.MAX_SPAWN_DELAY);
		antWorkerSpawnTimeSlider.setValue(1);
		root.getChildren().add(antWorkerSpawnTimeSlider);

		antWarriorSpawnTimeTextF.setMaxWidth(40);
		antWarriorSpawnTimeTextF.setLayoutX(890);
		antWarriorSpawnTimeTextF.setLayoutY(170);
		antWarriorSpawnTimeTextF.setText("1");
		root.getChildren().add(antWarriorSpawnTimeTextF);

		antWorkerSpawnTimeTextF.setMaxWidth(40);
		antWorkerSpawnTimeTextF.setLayoutX(890);
		antWorkerSpawnTimeTextF.setLayoutY(220);
		antWorkerSpawnTimeTextF.setText("1");
		root.getChildren().add(antWorkerSpawnTimeTextF);

		antWarriorLifeTimeTextF.setMaxWidth(60);
		antWarriorLifeTimeTextF.setLayoutX(810);
		antWarriorLifeTimeTextF.setLayoutY(270);
		antWarriorLifeTimeTextF.setText("10");
		root.getChildren().add(antWarriorLifeTimeTextF);

		antWorkerLifeTimeTextF.setMaxWidth(60);
		antWorkerLifeTimeTextF.setLayoutX(810);
		antWorkerLifeTimeTextF.setLayoutY(320);
		antWorkerLifeTimeTextF.setText("10");
		root.getChildren().add(antWorkerLifeTimeTextF);
	}

	private void menuSetUp() {
		menuBar.setMinWidth(940);
		menuBar.setMaxHeight(40);
		root.getChildren().add(menuBar);
		menuBar.getMenus().add(simulationMenu);
		simulationMenu.getItems().add(startMenuItem);
		simulationMenu.getItems().add(endMenuItem);
		simulationMenu.getItems().add(pauseMenuItem);
	}

	private void textSetUp() {
		Label text1 = new Label("Ant Warrior spawn chance");
		text1.setLayoutX(750);
		text1.setLayoutY(40);
		root.getChildren().add(text1);

		Label text2 = new Label("Ant Worker spawn chance");
		text2.setLayoutX(750);
		text2.setLayoutY(90);
		root.getChildren().add(text2);

		Label text3 = new Label("Ant Warrior spawn delay");
		text3.setLayoutX(758);
		text3.setLayoutY(150);
		root.getChildren().add(text3);

		Label text4 = new Label("Ant Worker spawn delay");
		text4.setLayoutX(758);
		text4.setLayoutY(200);
		root.getChildren().add(text4);

		Label text5 = new Label("Ant Warrior life time");
		text5.setLayoutX(782);
		text5.setLayoutY(250);
		root.getChildren().add(text5);

		Label text6 = new Label("Ant Worker life time");
		text6.setLayoutX(782);
		text6.setLayoutY(300);
		root.getChildren().add(text6);
	}

	private void alertsSetUp() {
		endSimulationAlert.setTitle("STATISTICS");
		endSimulationAlert.setContentText("End simulation?");

		invalidValueAlert.setTitle("Error");
		invalidValueAlert.setHeaderText("Invalid value");

		aliveAntsAlert.setTitle("Alive ants");
		aliveAntsAlert.setHeaderText(null);
	}

}
