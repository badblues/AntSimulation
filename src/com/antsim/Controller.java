package com.antsim;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    private static Controller instance;

    Model model = new Model();
    View view;
    Timer timer;

    private static synchronized Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    Controller() {}

    void init(View view) {
        this.view = view;
        setKeys();
        setButtonActions();
    }

    private Timer startSimulation() {
        view.getStartButton().setDisable(true);
        view.getPauseButton().setDisable(false);
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule((new TimerTask() {
            int tick = model.getTime();
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        update(tick);
                        tick++;
                    }
                });
            }
        }), 1000, 1000);
        return timer;
    }

    private void pauseSimulation() {
        view.getPauseButton().setDisable(true);
        view.getStartButton().setDisable(false);
        if (timer != null)
            timer.cancel();
    }

    private void endSimulation() {
        pauseSimulation();
        updateTimeText();
        for (Ant ant : model.getAntsArray()) {
            ant.destroyImage();
        }
        model.getAntsArray().clear();
        model.setAntWarriorCount(0);
        model.setAntWorkerCount(0);
        model.setTime(0);
    }

    void update(int time) {
        Random rand = new Random();
        model.setTimeToAntWarriorSpawn(model.getTimeToAntWarriorSpawn() - 1);
        model.setTimeToAntWorkerSpawn(model.getTimeToAntWorkerSpawn() - 1);
        model.setTime(time);
        updateTimeText();
        updateStatisticText();


        if (model.getTimeToAntWarriorSpawn() <= 0 &&
                rand.nextInt(100) <= model.getAntWarriorSpawnChance()) {
            AntWarrior a = new AntWarrior();
            a.spawn(view.getRoot());
            model.getAntsArray().add(a);
            model.setAntWarriorCount(model.getAntWarriorCount() + 1);
            model.setTimeToAntWarriorSpawn(model.getAntWarriorSpawnDelay());
        }

        if (model.getTimeToAntWorkerSpawn() <= 0 &&
                rand.nextInt(100) <= model.getAntWorkerSpawnChance()) {
            AntWorker a = new AntWorker();
            a.spawn(view.getRoot());
            model.getAntsArray().add(a);
            model.setAntWorkerCount(model.getAntWorkerCount() + 1);
            model.setTimeToAntWorkerSpawn(model.getAntWorkerSpawnDelay());
        }
    }

    private void setKeys() {
        view.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case B -> {
                        timer = startSimulation();
                    }
                    case E -> {
                        pauseSimulation();
                    }
                    case T ->  {
                        updateTimeText();
                        view.getTimeText().setVisible(!view.getTimeText().isVisible());
                    }
                }
            }
        });
    }

    private void setButtonActions() {
        view.getStartButton().setOnAction(actionEvent -> {
            timer = startSimulation();
        });

        view.getPauseButton().setOnAction(actionEvent -> {
            pauseSimulation();
        });

        view.getShowTimeButton().setOnAction(actionEvent -> {
            updateTimeText();
            view.getTimeText().setVisible(true);
        });

        view.getHideTimeButton().setOnAction(actionEvent -> {
            view.getTimeText().setVisible(false);
        });

        view.getShowInfoButton().setOnAction(actionEvent -> {
            pauseSimulation();
            updateStatisticText();
            view.getShowInfoButton().setSelected(true);
            Optional<ButtonType> option = view.getEndSimulationAlert().showAndWait();
            if (option.get() == ButtonType.OK)
                endSimulation();
            view.getShowInfoButton().setSelected(false);
        });

        view.getAntWarriorSpawnChanceBox().setOnAction(event -> {
            model.setAntWarriorSpawnChance((int)view.getAntWarriorSpawnChanceBox().getValue());
        });

        view.getAntWorkerSpawnChanceBox().setOnAction(event -> {
            model.setAntWorkerSpawnChance((int)view.getAntWorkerSpawnChanceBox().getValue());
        });

        view.getAntWarriorSpawnTimeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
                model.setAntWarriorSpawnDelay(newValue.intValue());
                model.setTimeToAntWarriorSpawn(newValue.intValue());
                view.getAntWarriorSpawnTimeTextF().setText("" + newValue.intValue());
            }
        });


        view.getAntWorkerSpawnTimeSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
                model.setAntWorkerSpawnDelay(newValue.intValue());
                model.setTimeToAntWorkerSpawn(newValue.intValue());
                view.getAntWorkerSpawnTimeTextF().setText("" + newValue.intValue());
            }
        });

        //Valid numbers check and input
        view.getAntWarriorSpawnTimeTextF().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.matches("\\d*"))
                    view.getAntWarriorSpawnTimeTextF().setText(newVal.replaceAll("\\D", ""));
                String s = view.getAntWarriorSpawnTimeTextF().getText();
                if (s.length() > 2) {
                    view.getAntWarriorSpawnTimeTextF().setText(s.substring(0, 2));
                }
            }
        });
        view.getAntWarriorSpawnTimeTextF().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int tmp = Integer.parseInt(view.getAntWarriorSpawnTimeTextF().getText());
                if (tmp >= Model.MIN_SPAWN_DELAY && tmp <= Model.MAX_SPAWN_DELAY) {
                    model.setAntWarriorSpawnDelay(tmp);
                    view.antWarriorSpawnTimeSlider.setValue(tmp);
                }
                else
                    view.getInvalidValueAlert().show();
            }
        });

        //Valid numbers check and input
        view.getAntWorkerSpawnTimeTextF().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.matches("\\d")) {
                    view.getAntWorkerSpawnTimeTextF().setText(newVal.replaceAll("\\D", ""));
                }
                String s = view.getAntWorkerSpawnTimeTextF().getText();
                if (s.length() > 2) {
                    view.getAntWorkerSpawnTimeTextF().setText(s.substring(0, 2));
                }
            }
        });
        view.getAntWorkerSpawnTimeTextF().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int tmp = Integer.parseInt(view.getAntWorkerSpawnTimeTextF().getText());
                if (tmp >= Model.MIN_SPAWN_DELAY && tmp <= Model.MAX_SPAWN_DELAY) {
                    model.setAntWorkerSpawnDelay(tmp);
                    view.antWorkerSpawnTimeSlider.setValue(tmp);
                }
                else
                    view.getInvalidValueAlert().show();
            }
        });

        view.getStartMenuItem().setOnAction(actionEvent -> {
            startSimulation();
        });



        view.getEndMenuItem().setOnAction(actionEvent -> {
            endSimulation();
            updateTimeText();
        });



        view.getPauseMenuItem().setOnAction(actionEvent -> {
            pauseSimulation();
        });

    }

    private void updateTimeText() {
        view.getTimeText().setText("Simulation time: " + model.getTime());
    }

    private void updateStatisticText() {
        view.getEndSimulationAlert().setHeaderText("Simulation time: " +
                model.getTime() + "\nWarriors: " + model.getAntWarriorCount() +
                "\nWorker: " + model.getAntWorkerCount());
    }

}
