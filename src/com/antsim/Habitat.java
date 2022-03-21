package com.antsim;

import javafx.application.Application;
import javafx.stage.Stage;

public class Habitat extends Application {

    private static Habitat instance;

    private static synchronized Habitat getInstance() {
        if (instance == null)
            instance = new Habitat();
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            View view = new View();
            view.init(stage);
            Controller controller = new Controller();
            controller.init(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
