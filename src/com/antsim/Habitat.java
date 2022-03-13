package com.antsim;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class Habitat extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 540, 540, Color.GREEN);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("AntSimulation");
        Image icon = new Image("D:\\Programming\\Projects\\4 semester\\AntSim\\resources\\icon_ant.jpg");
        stage.getIcons().add(icon);


        stage.show();
    }
}
