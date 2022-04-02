package com.antsim;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

	Habitat model = Habitat.getInstance();
	Timer timer;

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

	private boolean canShowStatistics = false;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		String[] chances = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
		antWarriorChanceBox.getItems().addAll(chances);
		antWorkerChanceBox.getItems().addAll(chances);

		antWarriorSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setAntWarriorSpawnDelay(newValue.intValue());
			model.setTimeToAntWarriorSpawn(newValue.intValue());
			antWarriorSpawnDelayTextF.setText("" + newValue.intValue());
		});


		antWarriorSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setAntWorkerSpawnDelay(newValue.intValue());
			model.setTimeToAntWorkerSpawn(newValue.intValue());
			antWarriorSpawnDelayTextF.setText("" + newValue.intValue());
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

		antWarriorSpawnDelayTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d*")) antWarriorSpawnDelayTextF.setText(newVal.replaceAll("\\D", ""));
			String s = antWarriorSpawnDelayTextF.getText();
			if(s.length() > 2) {
				antWarriorSpawnDelayTextF.setText(s.substring(0, 2));
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

		antWarriorLifeTimeTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				antWarriorLifeTimeTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = antWarriorLifeTimeTextF.getText();
			if(s.length() > 2)
				antWarriorLifeTimeTextF.setText(s.substring(0, 2));
		});
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
		ant.destroyImage();
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

		if(model.getTimeToAntWarriorSpawn() <= 0 && rand.nextInt(100) <= model.getAntWarriorSpawnChance()) {
			spawnWarrior(time);
		}

		if(model.getTimeToAntWorkerSpawn() <= 0 && rand.nextInt(100) <= model.getAntWorkerSpawnChance()) {
			spawnWorker(time);
		}
		checkLifeTime(time);
	}

	private void updateTimerText() {
		timerText.setText("Simulation time: " + model.getTime());
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

	public void startSimulation() {
		startButton.setDisable(true);
		pauseButton.setDisable(false);
		timer = startTimer();
	}


	public void pauseSimulation() {
		pauseButton.setDisable(true);
		startButton.setDisable(false);
		if(timer != null) timer.cancel();
		if(canShowStatistics) {
			//updateStatisticText();
			//TODO alert
			//			Optional<ButtonType> option = view.getEndSimulationAlert().showAndWait();
			//			if(option.get() == ButtonType.OK) endSimulation();
		}
	}

	private void endSimulation() {
		pauseSimulation();
		updateTimerText();
		for(Ant ant : model.getAntsVector()) {
			ant.destroyImage();
		}
		model.getAntsVector().clear();
		model.setAntWarriorCount(0);
		model.setAntWorkerCount(0);
		model.setTime(0);
	}

	//TODO keys
	//	private void setKeys() {
	//		view.getScene().setOnKeyPressed(keyEvent -> {
	//			switch(keyEvent.getCode()) {
	//				case B -> timer = startSimulation();
	//				case E -> pauseSimulation();
	//				case T -> {
	//					updateTimeText();
	//					view.getTimeText().setVisible(!view.getTimeText().isVisible());
	//				}
	//			}
	//		});
	//	}

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
		//TODO alert
		//		view.getAliveAntsAlert().setContentText(s);
		//		view.getAliveAntsAlert().show();
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
			//TODO alert
			//					view.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
			//					view.getInvalidValueAlert().show();
		}
	}

	public void changeAntWorkerSpawnDelayTextF() {
		int tmp = Integer.parseInt(antWorkerSpawnDelayTextF.getText());
		if(tmp >= Habitat.MIN_SPAWN_DELAY && tmp <= Habitat.MAX_SPAWN_DELAY) {
			model.setAntWorkerSpawnDelay(tmp);
			antWorkerSpawnDelaySlider.setValue(tmp);
		} else {
			//TODO alert
			//					view.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
			//					view.getInvalidValueAlert().show();
		}
	}

	public void changeAntWarriorLifeTime() {
		int tmp = Integer.parseInt(antWarriorLifeTimeTextF.getText());
		if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
			model.setAntWarriorLifeTime(tmp);
		} else {
			//TODO alert
			//					view.getInvalidValueAlert().setContentText("Choose value between "+Habitat.MIN_LIFE_TIME+" and "+Habitat.MAX_LIFE_TIME);
			//					view.getInvalidValueAlert().show();
		}
	}


	public void changeAntWorkerLifeTime() {

		int tmp = Integer.parseInt(antWorkerLifeTimeTextF.getText());
		if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
			model.setAntWorkerLifeTime(tmp);
		} else {
			//TODO alert
			//					view.getInvalidValueAlert().setContentText("Choose value between "+Habitat.MIN_LIFE_TIME+" and "+Habitat.MAX_LIFE_TIME);
			//					view.getInvalidValueAlert().show();
		}
	}


	//		view.getStartMenuItem().setOnAction(actionEvent -> startSimulation());
	//
	//
	//		view.getEndMenuItem().setOnAction(actionEvent -> {
	//			endSimulation();
	//			updateTimeText();
	//		});
	//
	//		view.getPauseMenuItem().setOnAction(actionEvent -> {
	//			pauseSimulation();
	//			if(canShowStatistics) {
	//				updateStatisticText();
	//				Optional<ButtonType> option = view.getEndSimulationAlert().showAndWait();
	//				if(option.get() == ButtonType.OK) endSimulation();
	//			}
	//		});

	//TODO Alert
	//	private void updateStatisticText() {
	//		view.getEndSimulationAlert().setHeaderText("Simulation time: " + model.getTime()
	//				+ "\nWarriors: " + model.getAntWarriorCount() + "\nWorker: " + model.getAntWorkerCount());
	//	}
}


