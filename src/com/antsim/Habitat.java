package com.antsim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Habitat extends Application {

    private static final int antWarriorSpawnChance = 100; // percents
    private static final int antWorkerSpawnChance = 100; // percents
    private static final int antWarriorSpawnDelay = 5; // seconds
    private static final int antWorkerSpawnDelay = 1; // seconds
    private ArrayList<Ants> antsArray = new ArrayList<Ants>();
    private Group root = new Group();
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

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case B:
                            break;
                    }
                }
            });


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                int time = 0;

                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            time += 1;
                            update(time);
                        }
                    });
                }
            }, 0, 1000);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(int time) {
        if (time % antWarriorSpawnDelay == 0) {
            AntWarrior a = new AntWarrior();
            a.spawn(root);
            antsArray.add(a);
        }
        if (time % antWorkerSpawnDelay == 0) {
            AntWorker a = new AntWorker();
            a.spawn(root);
            antsArray.add(a);
        }
    }

}
