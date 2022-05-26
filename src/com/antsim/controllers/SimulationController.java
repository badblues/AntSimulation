package com.antsim.controllers;

import com.antsim.ai.WarriorAI;
import com.antsim.ai.WorkerAI;
import com.antsim.ant.*;
import com.antsim.model.AntRequestDialog;
import com.antsim.model.Console;
import com.antsim.model.Habitat;
import com.antsim.view.AlertsView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class SimulationController implements Initializable {
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
	ComboBox<Integer> warriorChanceBox;
	@FXML
	ComboBox<Integer> workerChanceBox;
	@FXML
	Slider warriorSpawnDelaySlider;
	@FXML
	Slider workerSpawnDelaySlider;
	@FXML
	TextField warriorSpawnDelayTextF;
	@FXML
	TextField workerSpawnDelayTextF;
	@FXML
	TextField warriorLifeTimeTextF;
	@FXML
	TextField workerLifeTimeTextF;
	@FXML
	MenuItem startMenuItem;
	@FXML
	MenuItem pauseMenuItem;
	@FXML
	ComboBox<Integer> warriorThreadPriorityBox;
	@FXML
	ComboBox<Integer> workerThreadPriorityBox;
	@FXML
	ComboBox<Integer> mainThreadPriorityBox;
	@FXML
	CheckBox movementCheckbox;
	@FXML
	TextArea clientsText;

	Habitat model = Habitat.getInstance();
	AlertsView alertsView = AlertsView.getInstance();
	Timer timer;
	final WarriorAI warriorAI = new WarriorAI(model.getAntsVector());
	final WorkerAI antWorkerAI = new WorkerAI(model.getAntsVector());

	private boolean canShowStatistics = false;
	Console console;
	AntRequestDialog antRequestDialog;
	ConsoleController consoleController;
	AntRequestController antRequestController;

	LocalSaverController localSaverController = new LocalSaverController();
	SQLSaverController sqlSaverController = new SQLSaverController();
	ClientController clientController;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		Integer[] chances = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
		warriorChanceBox.getItems().addAll(chances);
		workerChanceBox.getItems().addAll(chances);

		Integer[] priorities = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		warriorThreadPriorityBox.getItems().addAll(priorities);
		workerThreadPriorityBox.getItems().addAll(priorities);
		mainThreadPriorityBox.getItems().addAll(priorities);

		warriorSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setWarriorSpawnDelay(newValue.intValue());
			model.setTimeToWarriorSpawn(newValue.intValue());
			warriorSpawnDelayTextF.setText("" + newValue.intValue());
		});


		workerSpawnDelaySlider.valueProperty().addListener((observableValue, number, newValue) -> {
			model.setWorkerSpawnDelay(newValue.intValue());
			model.setTimeToWorkerSpawn(newValue.intValue());
			workerSpawnDelayTextF.setText("" + newValue.intValue());
		});

		warriorSpawnDelayTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d*")) warriorSpawnDelayTextF.setText(newVal.replaceAll("\\D", ""));
			String s = warriorSpawnDelayTextF.getText();
			if(s.length() > 2) {
				warriorSpawnDelayTextF.setText(s.substring(0, 2));
			}
		});

		workerSpawnDelayTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				workerSpawnDelayTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = workerSpawnDelayTextF.getText();
			if(s.length() > 2) {
				workerSpawnDelayTextF.setText(s.substring(0, 2));
			}
		});

		warriorLifeTimeTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				warriorLifeTimeTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = warriorLifeTimeTextF.getText();
			if(s.length() > 2)
				warriorLifeTimeTextF.setText(s.substring(0, 2));
		});

		workerLifeTimeTextF.textProperty().addListener((observableValue, oldVal, newVal) -> {
			if(!newVal.matches("\\d")) {
				workerLifeTimeTextF.setText(newVal.replaceAll("\\D", ""));
			}
			String s = workerLifeTimeTextF.getText();
			if(s.length() > 2)
				workerLifeTimeTextF.setText(s.substring(0, 2));
		});

		setDefaultValuesToView();

		console = new Console();
		consoleController = console.getConsoleController();

		antRequestDialog = new AntRequestDialog();
		antRequestController = antRequestDialog.getAntRequestController();

		clientController = new ClientController(this, antRequestController, antsArea);

		launchAIThreads();
		clientController.start();
	}

	public void startSimulation() {
		startButton.setDisable(true);
		startMenuItem.setDisable(true);
		pauseButton.setDisable(false);
		pauseMenuItem.setDisable(false);
		timer = startTimer();
		movementCheckbox.setSelected(true);
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
		synchronized(model.getAntsVector()){
			for(Ant ant : model.getAntsVector()) {
				ant.destroyAnt();
			}
			model.getAntsVector().clear();
		}
		model.getAntsIdsHashSet().clear();
		model.getAntsSpawnTimeTree().clear();
		model.setWarriorCount(0);
		model.setWorkerCount(0);
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
			case C -> showConsoleArea();
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
		synchronized(model.getAntsVector()) {
			for(Ant ant : model.getAntsVector()) {
				int id = ant.getId();
				s = s.concat(ant.getClass().getSimpleName() + " ID:" + id + " - Spawn time: " + model.getAntsSpawnTimeTree().get(id) + "s\n");
			}
		}
		alertsView.getAliveAntsAlert().setContentText(s);
		alertsView.getAliveAntsAlert().show();
	}

	public void changeAntWarriorSpawnChance() {
		model.setWarriorSpawnChance(warriorChanceBox.getValue());
	}

	public void changeAntWorkerSpawnChance() {
		model.setWorkerSpawnChance(workerChanceBox.getValue());
	}

	public void changeAntWarriorSpawnDelayTextF() {
		if (!Objects.equals(warriorSpawnDelayTextF.getText(), "")) {
			int tmp = Integer.parseInt(warriorSpawnDelayTextF.getText());
			if(tmp >= Habitat.MIN_SPAWN_DELAY && tmp <= Habitat.MAX_SPAWN_DELAY) {
				model.setWarriorSpawnDelay(tmp);
				warriorSpawnDelaySlider.setValue(tmp);
			} else {
				alertsView.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
				alertsView.getInvalidValueAlert().show();
			}
		}
	}

	public void changeAntWorkerSpawnDelayTextF() {
		if (!Objects.equals(workerSpawnDelayTextF.getText(), "")) {
			int tmp = Integer.parseInt(workerSpawnDelayTextF.getText());
			if(tmp >= Habitat.MIN_SPAWN_DELAY && tmp <= Habitat.MAX_SPAWN_DELAY) {
				model.setWorkerSpawnDelay(tmp);
				workerSpawnDelaySlider.setValue(tmp);
			} else {
				alertsView.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_SPAWN_DELAY + " and " + Habitat.MAX_SPAWN_DELAY);
				alertsView.getInvalidValueAlert().show();
			}
		}
	}

	public void changeAntWarriorLifeTime() {
		if (!Objects.equals(warriorLifeTimeTextF.getText(), "")) {
			int tmp = Integer.parseInt(warriorLifeTimeTextF.getText());
			if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
				model.setWarriorLifeTime(tmp);
			} else {
				alertsView.getInvalidValueAlert().setContentText("Choose value between "+Habitat.MIN_LIFE_TIME+" and "+Habitat.MAX_LIFE_TIME);
				alertsView.getInvalidValueAlert().show();
			}
		}
	}


	public void changeAntWorkerLifeTime() {
		if (!Objects.equals(workerLifeTimeTextF.getText(), "")) {
			int tmp = Integer.parseInt(workerLifeTimeTextF.getText());
			if(tmp >= Habitat.MIN_LIFE_TIME && tmp <= Habitat.MAX_LIFE_TIME) {
				model.setWorkerLifeTime(tmp);
			} else {
				alertsView.getInvalidValueAlert().setContentText("Choose value between " + Habitat.MIN_LIFE_TIME +
						" and " + Habitat.MAX_LIFE_TIME);
				alertsView.getInvalidValueAlert().show();
			}
		}
	}

	public void changeAntWarriorThreadPriority() {
		model.setWarriorThreadPriority(warriorThreadPriorityBox.getValue());
		warriorAI.setPriority(model.getWarriorThreadPriority());
	}

	public void changeAntWorkerThreadPriority() {
		model.setWorkerThreadPriority(workerThreadPriorityBox.getValue());
		antWorkerAI.setPriority(model.getWorkerThreadPriority());
	}

	public void changeMainThreadPriority() {
		model.setMainThreadPriority(mainThreadPriorityBox.getValue());
		Thread.currentThread().setPriority(model.getMainThreadPriority());
	}

	public void changeMovement() {
		if (warriorAI.isPaused())
			startAllMovement();
		else
			stopAllMovement();
	}

	public void setDefaultValuesToView() {
		warriorChanceBox.setValue(model.getWarriorSpawnChance());
		workerChanceBox.setValue(model.getWorkerSpawnChance());
		warriorSpawnDelaySlider.setValue(model.getWarriorSpawnDelay());
		workerSpawnDelaySlider.setValue(model.getWorkerSpawnDelay());
		warriorSpawnDelayTextF.setText(Integer.toString(model.getWarriorSpawnDelay()));
		workerSpawnDelayTextF.setText(Integer.toString(model.getWorkerSpawnDelay()));
		warriorLifeTimeTextF.setText(Integer.toString(model.getWarriorLifeTime()));
		workerLifeTimeTextF.setText(Integer.toString(model.getWorkerLifeTime()));
		warriorThreadPriorityBox.setValue(model.getWarriorThreadPriority());
		warriorAI.setPriority(model.getWarriorThreadPriority());
		workerThreadPriorityBox.setValue(model.getWorkerThreadPriority());
		antWorkerAI.setPriority(model.getWorkerThreadPriority());
		mainThreadPriorityBox.setValue(model.getMainThreadPriority());
		Thread.currentThread().setPriority(model.getMainThreadPriority());
	}

	public void showConsoleArea() {
		consoleController.showConsole(this);
	}

	public void showAntRequestDialog() {
		antRequestController.showDialog(this);
	}


	public void saveAnts() {
		localSaverController.saveAll();
	}

	public void loadAnts() {
		pauseSimulation();
		endSimulation();
		localSaverController.loadAll(antsArea);
	}

	public void importAllFromDB() {
		pauseSimulation();
		endSimulation();
		sqlSaverController.loadAll(antsArea);
	}

	public void importWarriorsFromDB() {
		pauseSimulation();
		endSimulation();
		sqlSaverController.loadWarriors(antsArea);
	}

	public void importWorkersFromDB() {
		pauseSimulation();
		endSimulation();
		sqlSaverController.loadWorkers(antsArea);
	}

	public void exportAllToDB() {
		sqlSaverController.saveAll();
	}

	public void exportWarriorsToDB() {
		sqlSaverController.saveWarriors();
	}

	public void exportWorkersToDB() {
		sqlSaverController.saveWorkers();
	}

	public void updateClientsText() {
		ArrayList<Integer> array = Habitat.getInstance().getClientsArray();
		StringBuilder clients = new StringBuilder("Current client: ");
		if (!array.isEmpty()) {
			clients.append(array.get(0)).append("\n");
			for (int i = 1; i < array.size(); i++)
				clients.append("Client ").append(array.get(i)).append("\n");
		}
		clientsText.setText(clients.toString());
	}

	private int getNewID() {
		Random rand = new Random();
		int id = Math.abs(rand.nextInt(1000));
		while(model.getAntsIdsHashSet().contains(id)) id = Math.abs(rand.nextInt(1000));
		return id;
	}

	private void spawnWarrior(int time) {
		Warrior a = new Warrior();
		int id = getNewID();
		a.spawn(antsArea, time, model.getWarriorLifeTime(), id);
		synchronized(model.getAntsVector()){
			model.getAntsVector().add(a);
		}
		model.getAntsIdsHashSet().add(id);
		model.getAntsSpawnTimeTree().put(id, time);
		model.increaseWarriorCount(1);
		model.setTimeToWarriorSpawn(model.getWarriorSpawnDelay());
	}

	private void spawnWorker(int time) {
		Worker a = new Worker();
		int id = getNewID();
		a.spawn(antsArea, time, model.getWorkerLifeTime(), id);
		synchronized(model.getAntsVector()) {
			model.getAntsVector().add(a);
		}
		model.getAntsIdsHashSet().add(id);
		model.getAntsSpawnTimeTree().put(id, time);
		model.increaseWorkerCount(1);
		model.setTimeToWorkerSpawn(model.getWorkerSpawnDelay());
	}

	private void removeAnt(Ant ant) {
		ant.destroyAnt();
		model.getAntsIdsHashSet().remove(ant.getId());
		model.getAntsSpawnTimeTree().remove(ant.getId());
		synchronized(model.getAntsVector()) {
			model.getAntsVector().remove(ant);
		}
		if (ant instanceof Warrior)
			model.setWarriorCount(model.getWarriorCount() - 1);
		else
			model.setWorkerCount(model.getWorkerCount() - 1);
	}

	private void checkLifeTime(int time) {
		synchronized(model.getAntsVector()) {
			for(int i = 0; i < model.getAntsVector().size(); i++) {
				Ant ant = model.getAntsVector().get(i);
				if(ant.getSpawnTime() + ant.getLifeTime() == time) {
					removeAnt(ant);
					i--;
				}
			}
		}
	}


	private void update(int time) {
		Random rand = new Random();
		model.setTimeToWarriorSpawn(model.getTimeToWarriorSpawn() - 1);
		model.setTimeToWorkerSpawn(model.getTimeToWorkerSpawn() - 1);
		model.setTime(time);
		updateTimerText();
		if(model.getTimeToWarriorSpawn() <= 0 && rand.nextInt(100) < model.getWarriorSpawnChance()) {
			spawnWarrior(time);
		}
		if(model.getTimeToWorkerSpawn() <= 0 && rand.nextInt(100) < model.getWorkerSpawnChance()) {
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
				+ "\nWarriors: " + model.getWarriorCount() + "\nWorker: " + model.getWorkerCount());
	}

	private void launchAIThreads() {
		warriorAI.start();
		antWorkerAI.start();
	}

	private void stopAllMovement() {
		warriorAI.pause();
		antWorkerAI.pause();
	}

	private void startAllMovement() {
		if (startButton.isDisabled()) {  //if simulation stopped
			warriorAI.unpause();
			antWorkerAI.unpause();
		}
	}
}
