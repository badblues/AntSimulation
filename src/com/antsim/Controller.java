package com.antsim;

import com.antsim.ai.AntWarriorAI;
import com.antsim.ai.AntWorkerAI;
import com.antsim.ant.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

	Habitat model = Habitat.getInstance();
	final AntWarriorAI antWarriorAI = new AntWarriorAI(model.getAntsVector());
	final AntWorkerAI antWorkerAI = new AntWorkerAI(model.getAntsVector());
	AlertsView alertsView = AlertsView.getInstance();
	Timer timer;

	private boolean canShowStatistics = false;

	@FXML
	Group antsArea;
	@FXML
	Button startButton;
	@FXML
	Button pauseButton;
	@FXML
	Text timerText;
	@FXML
	RadioButton showTimeButton;
	@FXML
	RadioButton hideTimeButton;
	@FXML
	ComboBox<String> antWarriorChanceBox;
	@FXML
	ComboBox<String> antWorkerChanceBox;
	@FXML
	Slider antWarriorSpawnDelaySlider;
	@FXML
	Slider antWorkerSpawnDelaySlider;
	@FXML
	TextField antWarriorSpawnDelayTextF;
	@FXML
	TextField antWorkerSpawnDelayTextF;
	@FXML
	TextField antWarriorLifeTimeTextF;
	@FXML
	TextField antWorkerLifeTimeTextF;
	@FXML
	MenuItem startMenuItem;
	@FXML
	MenuItem pauseMenuItem;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		launchAIThreads();

		String[] chances = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
		antWarriorChanceBox.getItems().addAll(chances);
		antWarriorChanceBox.setValue("100");
		antWorkerChanceBox.getItems().addAll(chances);
		antWorkerChanceBox.setValue("100");

		antWarriorSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setAntWarriorSpawnDelay(newValue.intValue());
			model.setTimeToAntWarriorSpawn(newValue.intValue());
			antWarriorSpawnDelayTextF.setText("" + newValue.intValue());
		});


		antWorkerSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setAntWorkerSpawnDelay(newValue.intValue());
			model.setTimeToAntWorkerSpawn(newValue.intValue());
			antWorkerSpawnDelayTextF.setText("" + newValue.intValue());
		});

		antWarriorSpawnDelayTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d*")) antWarriorSpawnDelayTextF.setText(newVal.replaceAll("\\D", ""));
			String s = antWarriorSpawnDelayTextF.getText();
			if(s.length() > 2) {
				antWarriorSpawnDelayTextF.setText(s.substring(0, 2));
			}
		});

		antWorkerSpawnDelayTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				antWorkerSpawnDelayTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = antWorkerSpawnDelayTextF.getText();
			if(s.length() > 2) {
				antWorkerSpawnDelayTextF.setText(s.substring(0, 2));
			}
		});

		antWarriorLifeTimeTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				antWarriorLifeTimeTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = antWarriorLifeTimeTextF.getText();
			if(s.length() > 2)
				antWarriorLifeTimeTextF.setText(s.substring(0, 2));
		});

		antWorkerLifeTimeTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				antWorkerLifeTimeTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = antWorkerLifeTimeTextF.getText();
			if(s.length() > 2)
				antWorkerLifeTimeTextF.setText(s.substring(0, 2));
		});
	}

	public void startSimulation() {
		startButton.setDisable(true);
		startMenuItem.setDisable(true);
		pauseButton.setDisable(false);
		pauseMenuItem.setDisable(false);
		timer = startTimer();
		startAllMovement();
	}


	public void pauseSimulation() {
		pauseButton.setDisable(true);
		pauseMenuItem.setDisable(true);
		startButton.setDisable(false);
		startMenuItem.setDisable(false);
		if(timer != null) timer.cancel();
		stopAllMovement();
		if(canShowStatistics) {
			updateStatisticText();
			Optional<ButtonType> option = alertsView.getEndSimulationAlert().showAndWait();
				if(option.get() == ButtonType.OK)
					endSimulation();
		}
	}

	public void endSimulation() {
		pauseButton.setDisable(true);
		pauseMenuItem.setDisable(true);
		startButton.setDisable(false);
		startMenuItem.setDisable(false);
		if(timer != null) timer.cancel();
		for(Ant ant : model.getAntsVector()) {
			ant.destroyAnt();
		}
		model.getAntsVector().clear();
		model.getAntsIdsHashSet().clear();
		model.getAntsSpawnTimeTree().clear();
		model.setAntWarriorCount(0);
		model.setAntWorkerCount(0);
		model.setTime(0);
		updateTimerText();
	}


	public void keyEvent(KeyEvent keyEvent) {
		switch(keyEvent.getCode()) {
				case B -> startSimulation();
				case E -> pauseSimulation();
				case T -> {
					updateTimerText();
					timerText.setVisible(!timerText.isVisible());
					if (timerText.isVisible()) {
						showTimeButton.setSelected(true);
					} else {
						hideTimeButton.setSelected(true);
					}
				}
			}
		}

	public void showTimeText() {
		timerText.setVisible(true);
	}

	public void hideTimeText() {
		timerText.setVisible(false);
	}

	public void setShowInfo() {
		canShowStatistics = !canShowStatistics;
	}

	public void showAliveAnts() {
		pauseSimulation();
		String s = "Ants:\n";
		for(Ant ant : model.getAntsVector()) {
			int id = ant.getId();
			s = s.concat(ant.getClass().getSimpleName() + " ID:" + id + " - Spawn time: " + model.getAntsSpawnTimeTree().get(id) + "s\n");
		}
		alertsView.getAliveAntsAlert().setContentText(s);
		alertsView.getAliveAntsAlert().show();
	}

	public void changeAntWarriorSpawnChance() {
		model.setAntWarriorSpawnChance(Integer.parseInt(antWarriorChanceBox.getValue()));
	}

	public void changeAntWorkerSpawnChance() {
		model.setAntWorkerSpawnChance(Integer.parseInt(antWorkerChanceBox.getValue()));
	}

	public void changeAntWarriorSpawnDelayTextF() {
		int tmp = Integer.parseInt(antWarriorSpawnDelayTextF.getText());
		if(tmp >= Habitat.MIN_SPAWN_DELAY && tmp <= Habitat.MAX_SPAWN_DELAY) {
			model.setAntWarriorSpawnDelay(tmp);
			antWarriorSpawnDelaySlider.setValue(tmp);
		} else {
			alertsView.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
			alertsView.getInvalidValueAlert().show();
		}
	}

	public void changeAntWorkerSpawnDelayTextF() {
		int tmp = Integer.parseInt(antWorkerSpawnDelayTextF.getText());
		if(tmp >= Habitat.MIN_SPAWN_DELAY && tmp <= Habitat.MAX_SPAWN_DELAY) {
			model.setAntWorkerSpawnDelay(tmp);
			antWorkerSpawnDelaySlider.setValue(tmp);
		} else {
			alertsView.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
			alertsView.getInvalidValueAlert().show();
		}
	}

	public void changeAntWarriorLifeTime() {
		int tmp = Integer.parseInt(antWarriorLifeTimeTextF.getText());
		if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
			model.setAntWarriorLifeTime(tmp);
		} else {
			alertsView.getInvalidValueAlert().setContentText("Choose value between "+Habitat.MIN_LIFE_TIME+" and "+Habitat.MAX_LIFE_TIME);
			alertsView.getInvalidValueAlert().show();
		}
	}


	public void changeAntWorkerLifeTime() {

		int tmp = Integer.parseInt(antWorkerLifeTimeTextF.getText());
		if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
			model.setAntWorkerLifeTime(tmp);
		} else {
			alertsView.getInvalidValueAlert().setContentText("Choose value between "+Habitat.MIN_LIFE_TIME+" and "+Habitat.MAX_LIFE_TIME);
			alertsView.getInvalidValueAlert().show();
		}
	}

	private int getNewID() {
		Random rand = new Random();
		int id = Math.abs(rand.nextInt(1000));
		while(model.getAntsIdsHashSet().contains(id)) id = Math.abs(rand.nextInt(1000));
		return id;
	}

	private void spawnWarrior(int time) {
		AntWarrior a = new AntWarrior();
		int id = getNewID();
		a.spawn(antsArea, time, model.getAntWarriorLifeTime(), id);
		model.getAntsVector().add(a);
		model.getAntsIdsHashSet().add(id);
		model.getAntsSpawnTimeTree().put(id, time);
		model.setAntWarriorCount(model.getAntWarriorCount() + 1);
		model.setTimeToAntWarriorSpawn(model.getAntWarriorSpawnDelay());
	}

	private void spawnWorker(int time) {
		AntWorker a = new AntWorker();
		int id = getNewID();
		a.spawn(antsArea, time, model.getAntWorkerLifeTime(), id);
		model.getAntsVector().add(a);
		model.getAntsIdsHashSet().add(id);
		model.getAntsSpawnTimeTree().put(id, time);
		model.setAntWorkerCount(model.getAntWorkerCount() + 1);
		model.setTimeToAntWorkerSpawn(model.getAntWorkerSpawnDelay());
	}

	private void removeAnt(Ant ant) {
		ant.destroyAnt();
		model.getAntsIdsHashSet().remove(ant.getId());
		model.getAntsSpawnTimeTree().remove(ant.getId());
		model.getAntsVector().remove(ant);
		if(ant instanceof AntWarrior) model.setAntWarriorCount(model.getAntWarriorCount() - 1);
		else model.setAntWorkerCount(model.getAntWorkerCount() - 1);
	}

	private void checkLifeTime(int time) {
		for(int i = 0; i < model.getAntsVector().size(); i++) {
			Ant ant = model.getAntsVector().get(i);
			if(ant.getSpawnTime() + ant.getLifeTime() == time) {
				removeAnt(ant);
				i--;
			}
		}
	}

	private void update(int time) {
		Random rand = new Random();
		model.setTimeToAntWarriorSpawn(model.getTimeToAntWarriorSpawn() - 1);
		model.setTimeToAntWorkerSpawn(model.getTimeToAntWorkerSpawn() - 1);
		model.setTime(time);
		updateTimerText();

		if(model.getTimeToAntWarriorSpawn() <= 0 && rand.nextInt(100) < model.getAntWarriorSpawnChance()) {
			spawnWarrior(time);
		}

		if(model.getTimeToAntWorkerSpawn() <= 0 && rand.nextInt(100) < model.getAntWorkerSpawnChance()) {
			spawnWorker(time);
		}
		checkLifeTime(time);
	}

	private Timer startTimer() {
		if(timer != null) timer.cancel();
		timer = new Timer();
		timer.schedule((new TimerTask() {
			int tick = model.getTime();

			@Override
			public void run() {
				Platform.runLater(() -> {
					update(tick);
					tick++;
				});
			}
		}), 1000, 1000);
		return timer;
	}

	private void updateTimerText() {
		timerText.setText("Simulation time: " + model.getTime());
	}

	private void updateStatisticText() {
		alertsView.getEndSimulationAlert().setHeaderText("Simulation time: " + model.getTime()
				+ "\nWarriors: " + model.getAntWarriorCount() + "\nWorker: " + model.getAntWorkerCount());
	}

	private void launchAIThreads() {
		antWarriorAI.start();
		antWorkerAI.start();
	}

	private void stopAllMovement() {
		System.out.println("hyu govno1");
		try {
			synchronized(antWarriorAI) {
				antWarriorAI.wait();
			}
			synchronized(antWorkerAI) {
				antWorkerAI.wait();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("hyu govno2");
	}

	private void startAllMovement() {
		synchronized(antWarriorAI) {
			antWarriorAI.notify();
		}
		synchronized(antWorkerAI) {
			antWorkerAI.notify();
		}
	}

}


