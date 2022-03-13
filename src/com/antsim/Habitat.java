package com.antsim;

import javafx.application.Application;
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


public class Habitat extends Application {

    ArrayList<Ants> ants = new ArrayList<Ants>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("Habitat.fxml"));
            Group root = new Group();
            Color background = new Color(0.1, 0.5, 0.05, 1);
            Scene scene = new Scene(root, 540, 540, background);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("AntSimulation");
            Image icon = new Image("resources/icon_ant.jpg");
            stage.getIcons().add(icon);

            Image antWarrior = new Image("resources/ant_warrior.png");
            ImageView antWarriorImageView = new ImageView(antWarrior);
            Image antWorker = new Image("resources/ant_worker.png");
            ImageView antWorkerImageView = new ImageView(antWorker);

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch(keyEvent.getCode()) {
                        case B:
                            break;
                    }
                }
            });

            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
