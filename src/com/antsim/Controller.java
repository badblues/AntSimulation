package com.antsim;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;

import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    private static Controller instance;

    Model model = new Model();
    View view;
    Timer timer;
    boolean canShowStatistics = false;

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
        for (Ant ant : model.getAntsVector()) {
            ant.destroyImage();
        }
        model.getAntsVector().clear();
        model.setAntWarriorCount(0);
        model.setAntWorkerCount(0);
        model.setTime(0);
    }

    int getNewID() {
        Random rand = new Random();
        int id = rand.nextInt();
        while (model.getAntsIds().contains(id))
            id = rand.nextInt();
        return id;
    }

    void spawnWarrior(int time) {
        AntWarrior a = new AntWarrior();
        int id = getNewID();
        a.spawn(view.getRoot(), time, model.getAntWarriorLifeTime(), id);
        model.getAntsVector().add(a);
        model.getAntsIds().add(id);
        model.getAntsSpawnTime().put(id, time);
        model.setAntWarriorCount(model.getAntWarriorCount() + 1);
        model.setTimeToAntWarriorSpawn(model.getAntWarriorSpawnDelay());
    }

    void spawnWorker(int time) {
        AntWorker a = new AntWorker();
        int id = getNewID();
        a.spawn(view.getRoot(), time, model.getAntWorkerLifeTime(), id);
        model.getAntsVector().add(a);
        model.getAntsIds().add(id);
        model.getAntsSpawnTime().put(id, time);
        model.setAntWorkerCount(model.getAntWorkerCount() + 1);
        model.setTimeToAntWorkerSpawn(model.getAntWorkerSpawnDelay());
    }

    void removeAnt(Ant ant) {
        ant.destroyImage();
        model.getAntsIds().remove(ant.getId());
        model.getAntsSpawnTime().remove(ant.getId());
        model.getAntsVector().remove(ant);
        if (ant instanceof AntWarrior)
            model.setAntWarriorCount(model.getAntWarriorCount() - 1);
        else
            model.setAntWorkerCount(model.getAntWorkerCount() - 1);
    }

    void checkLifeTime(int time) {
        for (int i = 0; i < model.getAntsVector().size(); i++) {
            Ant ant = model.getAntsVector().get(i);
            if (ant.getSpawnTime() + ant.getLifeTime() == time) {
                removeAnt(ant);
                i--;
            }
        }
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
            spawnWarrior(time);
        }

        if (model.getTimeToAntWorkerSpawn() <= 0 &&
                rand.nextInt(100) <= model.getAntWorkerSpawnChance()) {
            spawnWorker(time);
        }
        checkLifeTime(time);
    }

    private void setKeys() {
        view.getScene().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case B -> timer = startSimulation();
                case E -> pauseSimulation();
                case T ->  {
                    updateTimeText();
                    view.getTimeText().setVisible(!view.getTimeText().isVisible());
                }
            }
        });
    }

    private void setButtonActions() {
        view.getStartButton().setOnAction(actionEvent ->  timer = startSimulation());

        view.getPauseButton().setOnAction(actionEvent -> {
            pauseSimulation();
            if (canShowStatistics) {
                updateStatisticText();
                Optional<ButtonType> option = view.getEndSimulationAlert().showAndWait();
                if (option.get() == ButtonType.OK)
                    endSimulation();
            }
        });

        view.getShowTimeButton().setOnAction(actionEvent -> {
            updateTimeText();
            view.getTimeText().setVisible(true);
        });

        view.getHideTimeButton().setOnAction(actionEvent -> view.getTimeText().setVisible(false));

        view.getShowInfoButton().setOnAction(actionEvent -> {
            canShowStatistics = !canShowStatistics;
            view.getShowInfoButton().setSelected(canShowStatistics);
        });

        view.getAntWarriorSpawnChanceBox().setOnAction(event -> model.setAntWarriorSpawnChance(view.getAntWarriorSpawnChanceBox().getValue()));

        view.getAntWorkerSpawnChanceBox().setOnAction(event -> model.setAntWorkerSpawnChance(view.getAntWorkerSpawnChanceBox().getValue()));

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
                    view.getAntWarriorSpawnTimeSlider().setValue(tmp);
                }
                else {
                    view.getInvalidValueAlert().setContentText("Choose value between " + Model.MIN_SPAWN_DELAY +
                            " and " + Model.MAX_SPAWN_DELAY);
                    view.getInvalidValueAlert().show();
                }
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
                    view.getAntWorkerSpawnTimeSlider().setValue(tmp);
                }
                else {
                    view.getInvalidValueAlert().setContentText("Choose value between " + Model.MIN_SPAWN_DELAY +
                            " and " + Model.MAX_SPAWN_DELAY);
                    view.getInvalidValueAlert().show();
                }
            }
        });

        view.getAntWarriorLifeTimeTextF().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.matches("\\d")) {
                    view.getAntWarriorLifeTimeTextF().setText(newVal.replaceAll("\\D", ""));
                }
                String s = view.getAntWarriorLifeTimeTextF().getText();
                if (s.length() > 2)
                    view.getAntWarriorLifeTimeTextF().setText(s.substring(0, 2));
            }
        });
        view.getAntWarriorLifeTimeTextF().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int tmp = Integer.parseInt(view.getAntWarriorLifeTimeTextF().getText());
                if (tmp >= Model.MIN_LIFE_TIME && tmp <= Model.MAX_LIFE_TIME) {
                    model.setAntWarriorLifeTime(tmp);
                }
                else {
                    view.getInvalidValueAlert().setContentText("Choose value between " + Model.MIN_LIFE_TIME +
                            " and " + Model.MAX_LIFE_TIME);
                    view.getInvalidValueAlert().show();
                }
            }
        });

        view.getAntWorkerLifeTimeTextF().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.matches("\\d")) {
                    view.getAntWorkerLifeTimeTextF().setText(newVal.replaceAll("\\D", ""));
                }
                String s = view.getAntWorkerLifeTimeTextF().getText();
                if (s.length() > 2)
                    view.getAntWorkerLifeTimeTextF().setText(s.substring(0, 2));
            }
        });
        view.getAntWorkerLifeTimeTextF().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int tmp = Integer.parseInt(view.getAntWorkerLifeTimeTextF().getText());
                if (tmp >= Model.MIN_LIFE_TIME && tmp <= Model.MAX_LIFE_TIME) {
                    model.setAntWorkerLifeTime(tmp);
                }
                else {
                    view.getInvalidValueAlert().setContentText("Choose value between " + Model.MIN_LIFE_TIME +
                            " and " + Model.MAX_LIFE_TIME);
                    view.getInvalidValueAlert().show();
                }
            }
        });


        view.getStartMenuItem().setOnAction(actionEvent -> startSimulation());


        view.getEndMenuItem().setOnAction(actionEvent -> {
            endSimulation();
            updateTimeText();
        });

        view.getPauseMenuItem().setOnAction(actionEvent -> {
            pauseSimulation();
            if (canShowStatistics) {
                updateStatisticText();
                Optional<ButtonType> option = view.getEndSimulationAlert().showAndWait();
                if (option.get() == ButtonType.OK)
                    endSimulation();
            }
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
