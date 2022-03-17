package com.antsim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//TODO simulation pause with continuation --------
//TODO singleton
//TODO divide window by two areas --------
//TODO add Start and Stop buttons
//TODO add switch Show info
//TODO add group of two exclusive switches Show sim time and Hide sim time
//TODO add main menu and tool panel
//TODO add modal dialog window with info about generated objects and time, window has 2 buttons OK and Cancel
//TODo add control elements spawn delay and spawn chance


public class Habitat extends Application {

    static final int antWarriorSpawnChance = 50; // percents
    static final int antWorkerSpawnChance = 50; // percents
    static final int antWarriorSpawnDelay = 3; // seconds
    static final int antWorkerSpawnDelay = 1; // seconds
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
                            showStatistic();
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
        startButton.setLayoutX(50);
        startButton.setLayoutY(50);
        startButton.setOnAction(actionEvent ->  {
            timer = startTimer();
            statisticText.setVisible(false);
        });
        root.getChildren().add(startButton);

        Button stopButton = new Button("Stop");
        stopButton.setLayoutX(100);
        stopButton.setLayoutY(50);
        stopButton.setOnAction(actionEvent -> {
            endTimer();
            showStatistic();
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
    }
}
