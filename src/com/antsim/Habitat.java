package com.antsim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//TODO simulation pause with continuation
//TODO format text


public class Habitat extends Application {

    static final int antWarriorSpawnChance = 50; // percents
    static final int antWorkerSpawnChance = 50; // percents
    static final int antWarriorSpawnDelay = 2; // seconds
    static final int antWorkerSpawnDelay = 1; // seconds
    ArrayList<Ant> antsArray = new ArrayList<Ant>();
    int antWarriorCount = 0;
    int antWorkerCount = 0;
    boolean isWorking = false;
    int time;
    Group root = new Group();
    Text timeText = new Text("Simulation time:   ");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {

            Color background = new Color(0.1, 0.5, 0.05, 1);
            Scene scene = new Scene(root, 540, 540, background);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("AntSimulation");
            Image icon = new Image("resources/icon_ant.jpg");
            stage.getIcons().add(icon);
            root.getChildren().add(timeText);
            timeText.setX(220);
            timeText.setY(20);
            timeText.setFont(Font.font("Verdana"));
            timeText.setFill(Color.WHITE);
            timeText.setVisible(false);

            Timer timer = new Timer();

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case B:
                            if (!isWorking) {
                                startTimer(timer);
                                isWorking = true;
                            }
                            break;
                        case E:
                            if (isWorking) {
                                timer.cancel();
                                showStatistic();
                            }
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

    private void startTimer(Timer timer) {
        timer.schedule(new TimerTask() {
            int tick = 0;

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
        }, 0, 1000);
    }

    private void showStatistic() {
        Text antCounter = new Text("STATISTICS:\nSimulation time: " + time + "s\nWorkers born: " + antWorkerCount + "\nWarriors born: " + antWarriorCount);
        antCounter.setFont(Font.font("Verdana"));
        antCounter.setFill(Color.WHITE);
        antCounter.setX(400);
        antCounter.setY(240);
        root.getChildren().add(antCounter);
    }
}
