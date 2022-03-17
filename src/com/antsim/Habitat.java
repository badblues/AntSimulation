package com.antsim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

//TODO simulation pause with continuation --------
//TODO singleton
//TODO divide window by two areas --------
//TODO add Start and Stop buttons --------
//TODO add switch Show info
//TODO add group of two exclusive switches Show sim time and Hide sim time --------
//TODO add main menu and tool panel --------
//TODO add modal dialog window with info about generated objects and time, window has 2 buttons OK and Cancel
//TODo add control elements spawn delay and spawn chance


public class Habitat extends Application {

    static int antWarriorSpawnChance = 50; // percents
    static int antWorkerSpawnChance = 50; // percents
    static int antWarriorSpawnDelay = 1; // seconds
    static int antWorkerSpawnDelay = 1; // seconds
    ArrayList<Ant> antsArray = new ArrayList<Ant>();
    int antWarriorCount = 0;
    int antWorkerCount = 0;
    int time;
    Group root = new Group();
    Scene scene = new Scene(root, 740, 540);
    Text timeText = new Text("Simulation time:   ");
    Text statisticText = new Text("");
    Timer timer;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stageSetUp(stage);
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case B:
                            timer = startTimer();
                            statisticText.setVisible(false);
                            break;
                        case E:
                            endTimer();
                            //showStatistic();
                            break;
                        case T:
                            timeText.setVisible(!timeText.isVisible());
                            break;
                    }
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(int tick) {
        Random rand = new Random();
        if (tick % antWarriorSpawnDelay == 0 && rand.nextInt(100) < antWarriorSpawnChance) {
            AntWarrior a = new AntWarrior();
            a.spawn(root);
            antsArray.add(a);
            antWarriorCount += 1;
        }
        if (tick % antWorkerSpawnDelay == 0 && rand.nextInt(100) < antWorkerSpawnChance) {
            AntWorker a = new AntWorker();
            a.spawn(root);
            antsArray.add(a);
            antWorkerCount += 1;
        }
        time = tick;
        timeText.setText("Simulation time: " + time + "s");
    }

    private Timer startTimer(){
        endTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int tick = time;
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        update(tick);
                        tick += 1;
                    }
                });
            }
        }, 1000, 1000);
        return timer;
    }

    private void endTimer() {
        if (timer != null)
            timer.cancel();
    }

    private void showStatistic() {
        statisticText.setText("STATISTICS\nSimulation time: " + time + "s\nWorkers born: " + antWorkerCount + "\nWarriors born :" + antWarriorCount);
        statisticText.setVisible(true);
    }

    private void showInfo(CheckBox button) {
        endTimer();
        Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("STATISTICS");
        alert.setHeaderText("Simulation time: " + time + "s\nWorkers born: " + antWorkerCount + "\nWarriors born :" + antWarriorCount);
        alert.setContentText("End simulation?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.CANCEL)
            startTimer();
        button.setSelected(false);
    }

    private void stageSetUp(Stage stage) {
        scene.setFill(new Color(0.63, 0.39, 0.24, 1));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("AntSimulation");
        Image icon = new Image("resources/icon_ant.jpg");
        stage.getIcons().add(icon);

        timeText.setX(370);
        timeText.setY(20);
        timeText.setFont(Font.font("Verdana"));
        timeText.setFill(Color.WHITE);
        timeText.setVisible(false);
        root.getChildren().add(timeText);

        statisticText.setX(600);
        statisticText.setY(270);
        statisticText.setFont(Font.font("Verdana"));
        statisticText.setFill(Color.WHITE);
        statisticText.setVisible(false);
        root.getChildren().add(statisticText);

        Rectangle area = new Rectangle(0, 0, 200, 540);
        area.setFill(Color.GREY);
        root.getChildren().add(area);

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");

        startButton.setLayoutX(50);
        startButton.setLayoutY(50);
        startButton.setOnAction(actionEvent ->  {
            timer = startTimer();
            statisticText.setVisible(false);
            startButton.setDisable(true);
            stopButton.setDisable(false);
        });
        root.getChildren().add(startButton);

        stopButton.setLayoutX(100);
        stopButton.setLayoutY(50);
        stopButton.setOnAction(actionEvent -> {
            endTimer();
            stopButton.setDisable(true);
            startButton.setDisable(false);
            //showStatistic();
        });
        root.getChildren().add(stopButton);

        ToggleGroup timeButtonsGroup = new ToggleGroup();

        RadioButton showTimeButton = new RadioButton("Show time");
        showTimeButton.setLayoutX(30);
        showTimeButton.setLayoutY(100);
        showTimeButton.setOnAction(actionEvent -> {
            timeText.setVisible(!timeText.isVisible());
        });
        showTimeButton.setToggleGroup(timeButtonsGroup);
        root.getChildren().add(showTimeButton);

        RadioButton hideTimeButton = new RadioButton("Hide time");
        hideTimeButton.setLayoutX(30);
        hideTimeButton.setLayoutY(120);
        hideTimeButton.setOnAction(actionEvent -> {
            timeText.setVisible(!timeText.isVisible());
        });
        hideTimeButton.setToggleGroup(timeButtonsGroup);
        hideTimeButton.setSelected(true);
        root.getChildren().add(hideTimeButton);
        
        CheckBox showInfoButton = new CheckBox("Show info");
        showInfoButton.setLayoutX(30);
        showInfoButton.setLayoutY(140);
        showInfoButton.setOnAction(actionEvent -> {
            showInfo(showInfoButton);
        });
        root.getChildren().add(showInfoButton);

        ObservableList<String> chances = FXCollections.observableArrayList("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        ComboBox<String> antWarriorSpawnChanceBox = new ComboBox<String>(chances);
        antWarriorSpawnChanceBox.setValue("50");
        antWarriorSpawnChanceBox.setOnAction(event -> {
            antWarriorSpawnChance = Integer.parseInt(antWarriorSpawnChanceBox.getValue());
        });
        antWarriorSpawnChanceBox.setLayoutX(30);
        antWarriorSpawnChanceBox.setLayoutY(200);
        root.getChildren().add(antWarriorSpawnChanceBox);

        ComboBox<String> antWorkerSpawnChanceBox = new ComboBox<String>(chances);
        antWorkerSpawnChanceBox.setValue("50");
        antWorkerSpawnChanceBox.setOnAction(event -> {
            antWorkerSpawnChance = Integer.parseInt(antWorkerSpawnChanceBox.getValue());
        });
        antWorkerSpawnChanceBox.setLayoutX(30);
        antWorkerSpawnChanceBox.setLayoutY(250);
        root.getChildren().add(antWorkerSpawnChanceBox);

        Label text = new Label("Ant Warrior spawn chance");
        text.setLayoutX(30);
        text.setLayoutY(180);
        root.getChildren().add(text);

        Label text2 = new Label("Ant Worker spawn chance");
        text2.setLayoutX(30);
        text2.setLayoutY(230);
        root.getChildren().add(text2);

        Slider antWarriorTimeToSpawn = new Slider();
        antWarriorTimeToSpawn.setMin(1);
        antWarriorTimeToSpawn.setMax(100);
        antWarriorTimeToSpawn.setMinorTickCount(1);
        antWarriorTimeToSpawn.setOnDragDone(dragEvent -> {
            antWarriorSpawnDelay = (int) antWarriorTimeToSpawn.getValue();
        });
        root.getChildren().add(antWarriorTimeToSpawn);
    }
}
