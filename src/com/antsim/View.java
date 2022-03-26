package com.antsim;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class View {

    private static View instance;

    private Stage stage;
    private Group root = new Group();
    private Scene scene = new Scene(root, 740, 540);
    private Rectangle toolArea = new Rectangle(0, 0, 200, 540);
    private Text timeText = new Text("Simulation time:   ");
    private Button startButton = new Button("Start");
    private Button pauseButton = new Button("Pause");
    private Alert endSimulationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert invalidValueAlert = new Alert(Alert.AlertType.ERROR);
    private ToggleGroup timeButtonsGroup = new ToggleGroup();
    private RadioButton showTimeButton = new RadioButton("Show time");
    private RadioButton hideTimeButton = new RadioButton("Hide time");
    private CheckBox showInfoButton = new CheckBox("Show info");
    private ComboBox<Integer> antWarriorSpawnChanceBox = new ComboBox<Integer>();
    private ComboBox<Integer> antWorkerSpawnChanceBox = new ComboBox<Integer>();
    private Slider antWarriorSpawnTimeSlider = new Slider();
    private Slider antWorkerSpawnTimeSlider = new Slider();
    private TextField antWarriorSpawnTimeTextF = new TextField();
    private TextField antWorkerSpawnTimeTextF = new TextField();
    private TextField antWarriorLifeTimeTextF = new TextField();
    private TextField antWorkerLifeTimeTextF = new TextField();
    private MenuBar menuBar = new MenuBar();
    private Menu simulationMenu = new Menu("Simulation");
    private MenuItem startMenuItem = new MenuItem("Start");
    private MenuItem endMenuItem = new MenuItem("End");
    private MenuItem pauseMenuItem = new MenuItem("Pause");

    private static synchronized View getInstance() {
        if (instance == null)
            instance = new View();
        return instance;
    }

    View() {}

    void init(Stage stage) {
        this.stage = stage;
        timeTextSetUp();
        backgroundSetUp();
        stageSetUp();
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

    Alert getEndSimulationAlert() {
        return endSimulationAlert;
    }

    Alert getInvalidValueAlert() { return invalidValueAlert; }

    RadioButton getShowTimeButton() {
        return showTimeButton;
    }

    RadioButton getHideTimeButton() {
        return hideTimeButton;
    }

    CheckBox getShowInfoButton() {
        return showInfoButton;
    }

    ComboBox<Integer> getAntWarriorSpawnChanceBox() { return antWarriorSpawnChanceBox; }

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

    TextField getAntWarriorLifeTimeTextF() { return antWarriorLifeTimeTextF; }

    TextField getAntWorkerLifeTimeTextF() { return antWorkerLifeTimeTextF; }

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
        scene.setFill(new Color(0.63, 0.39, 0.24, 1));
        Rectangle area = new Rectangle(0, 0, 200, 540);
        area.setFill(Color.GREY);
        root.getChildren().add(area);
    }

    private void stageSetUp() {

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("AntSimulation");
        Image icon = new Image("resources/icon_ant.jpg");
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
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
        root.getChildren().add(showInfoButton);

        ObservableList<Integer> chances = FXCollections.observableArrayList(
                0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);

        antWarriorSpawnChanceBox.setItems(chances);
        antWarriorSpawnChanceBox.setLayoutX(30);
        antWarriorSpawnChanceBox.setLayoutY(200);
        antWarriorSpawnChanceBox.setValue(100);
        root.getChildren().add(antWarriorSpawnChanceBox);

        antWorkerSpawnChanceBox.setItems(chances);
        antWorkerSpawnChanceBox.setLayoutX(30);
        antWorkerSpawnChanceBox.setLayoutY(250);
        antWorkerSpawnChanceBox.setValue(100);
        root.getChildren().add(antWorkerSpawnChanceBox);

        antWarriorSpawnTimeSlider.setLayoutX(10);
        antWarriorSpawnTimeSlider.setLayoutY(310);
        antWarriorSpawnTimeSlider.setShowTickMarks(true);
        antWarriorSpawnTimeSlider.setMinorTickCount(0);
        antWarriorSpawnTimeSlider.setMajorTickUnit(1);
        antWarriorSpawnTimeSlider.setSnapToTicks(true);
        antWarriorSpawnTimeSlider.setMin(Model.MIN_SPAWN_DELAY);
        antWarriorSpawnTimeSlider.setMax(Model.MAX_SPAWN_DELAY);
        antWarriorSpawnTimeSlider.setValue(1);
        root.getChildren().add(antWarriorSpawnTimeSlider);

        antWorkerSpawnTimeSlider.setLayoutX(10);
        antWorkerSpawnTimeSlider.setLayoutY(360);
        antWorkerSpawnTimeSlider.setShowTickMarks(true);
        antWorkerSpawnTimeSlider.setMajorTickUnit(1);
        antWorkerSpawnTimeSlider.setMinorTickCount(0);
        antWorkerSpawnTimeSlider.setSnapToTicks(true);
        antWorkerSpawnTimeSlider.setMin(Model.MIN_SPAWN_DELAY);
        antWorkerSpawnTimeSlider.setMax(Model.MAX_SPAWN_DELAY);
        antWorkerSpawnTimeSlider.setValue(1);
        root.getChildren().add(antWorkerSpawnTimeSlider);

        antWarriorSpawnTimeTextF.setMaxWidth(40);
        antWarriorSpawnTimeTextF.setLayoutX(150);
        antWarriorSpawnTimeTextF.setLayoutY(310);
        antWarriorSpawnTimeTextF.setText("1");
        root.getChildren().add(antWarriorSpawnTimeTextF);

        antWorkerSpawnTimeTextF.setMaxWidth(40);
        antWorkerSpawnTimeTextF.setLayoutX(150);
        antWorkerSpawnTimeTextF.setLayoutY(360);
        antWorkerSpawnTimeTextF.setText("1");
        root.getChildren().add(antWorkerSpawnTimeTextF);

        antWarriorLifeTimeTextF.setMaxWidth(60);
        antWarriorLifeTimeTextF.setLayoutX(40);
        antWarriorLifeTimeTextF.setLayoutY(410);
        antWarriorLifeTimeTextF.setText("10");
        root.getChildren().add(antWarriorLifeTimeTextF);

        antWorkerLifeTimeTextF.setMaxWidth(60);
        antWorkerLifeTimeTextF.setLayoutX(40);
        antWorkerLifeTimeTextF.setLayoutY(450);
        antWorkerLifeTimeTextF.setText("10");
        root.getChildren().add(antWorkerLifeTimeTextF);
    }

    private void menuSetUp() {
        menuBar.setMinWidth(740);
        menuBar.setMaxHeight(40);
        root.getChildren().add(menuBar);
        menuBar.getMenus().add(simulationMenu);
        simulationMenu.getItems().add(startMenuItem);
        simulationMenu.getItems().add(endMenuItem);
        simulationMenu.getItems().add(pauseMenuItem);
    }

    private void textSetUp() {
        Label text = new Label("Ant Warrior spawn chance");
        text.setLayoutX(25);
        text.setLayoutY(180);
        root.getChildren().add(text);

        Label text2 = new Label("Ant Worker spawn chance");
        text2.setLayoutX(25);
        text2.setLayoutY(230);
        root.getChildren().add(text2);

        Label text3 = new Label("Ant Warrior spawn delay");
        text3.setLayoutX(25);
        text3.setLayoutY(290);
        root.getChildren().add(text3);

        Label text4 = new Label("Ant Worker spawn delay");
        text4.setLayoutX(25);
        text4.setLayoutY(340);
        root.getChildren().add(text4);

        Label text5 = new Label("Ant Warrior life time");
        text5.setLayoutX(25);
        text5.setLayoutY(390);
        root.getChildren().add(text5);

        Label text6 = new Label("Ant Worker life time");
        text6.setLayoutX(25);
        text6.setLayoutY(435);
        root.getChildren().add(text6);
    }

    private void alertsSetUp() {
        endSimulationAlert.setTitle("STATISTICS");
        endSimulationAlert.setContentText("End simulation?");

        invalidValueAlert.setTitle("Error");
        invalidValueAlert.setHeaderText("Invalid value");
    }

}
