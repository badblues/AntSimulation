package com.antsim.model;

import com.antsim.controllers.ConfigController;
import com.antsim.ant.*;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

import java.util.*;


public class Habitat extends Application {

	static final public int MAX_SPAWN_DELAY = 30;
	static final public int MIN_SPAWN_DELAY = 1;
	static final public int MIN_LIFE_TIME = 1;
	static final public int MAX_LIFE_TIME = 20;
	private static Habitat instance;
	private static ConfigController configController = new ConfigController();
	private final Vector<Ant> antsVector = new Vector<>();
	private final HashSet<Integer> antsIds = new HashSet<>();
	private final TreeMap<Integer, Integer> antsSpawnTime = new TreeMap<>();
	private int warriorSpawnChance = 100; //percents
	private int workerSpawnChance = 100; //percents
	private int warriorSpawnDelay = 1; // seconds
	private int workerSpawnDelay = 1; // seconds
	private int warriorCount;
	private int workerCount;
	private int time;
	private int timeToWarriorSpawn = warriorSpawnDelay;
	private int timeToWorkerSpawn = workerSpawnDelay;
	private int warriorLifeTime = 10;
	private int workerLifeTime = 10;
	private int warriorThreadPriority = 5;
	private int workerThreadPriority = 5;
	private int mainThreadPriority = 5;

	public static synchronized Habitat getInstance() {
		if(instance == null)
			instance = new Habitat();
		return instance;
	}

	public static void main(String[] args) {
		configController.load();
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AntSim.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setOnCloseRequest(windowEvent ->  {
				configController.save();
				Platform.exit();
				System.exit(0);
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Vector<Ant> getAntsVector() {
		return antsVector;
	}

	public HashSet<Integer> getAntsIdsHashSet() {
        return antsIds;
    }

	public TreeMap<Integer, Integer> getAntsSpawnTimeTree() {
        return antsSpawnTime;
    }

	public int getWarriorSpawnChance() {
		return warriorSpawnChance;
	}

	public void setWarriorSpawnChance(int chance) {
		warriorSpawnChance = chance;
	}

	public int getWorkerSpawnChance() {
		return workerSpawnChance;
	}

	public void setWorkerSpawnChance(int chance) {
		workerSpawnChance = chance;
	}

	public int getWarriorSpawnDelay() {
		return warriorSpawnDelay;
	}

	public void setWarriorSpawnDelay(int delay) {
		warriorSpawnDelay = delay;
	}

	public int getWorkerSpawnDelay() {return workerSpawnDelay;}

	public void setWorkerSpawnDelay(int delay) {
		workerSpawnDelay = delay;
	}

	public int getWarriorCount() {
		return warriorCount;
	}

	public void setWarriorCount(int count) {
		warriorCount = count;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int count) {
		workerCount = count;
	}

	public int getTime() {
		return time;
	}

	public void increaseWarriorCount(int i) {
		warriorCount += i;
	}

	public void increaseWorkerCount(int i) {
		workerCount += i;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTimeToWarriorSpawn() {
		return timeToWarriorSpawn;
	}

	public void setTimeToWarriorSpawn(int time) {
		timeToWarriorSpawn = time;}

	public int getTimeToWorkerSpawn() {
		return timeToWorkerSpawn;
	}

	public void setTimeToWorkerSpawn(int time) {
        timeToWorkerSpawn = time;
    }

	public int getWarriorLifeTime() {
        return warriorLifeTime;
    }

	public void setWarriorLifeTime(int time) {
        warriorLifeTime = time;
    }

	public int getWorkerLifeTime() {
        return workerLifeTime;
    }

	public void setWorkerLifeTime(int time) {
        workerLifeTime = time;
    }

	public void setWarriorThreadPriority(int warriorThreadPriority) {
		this.warriorThreadPriority = warriorThreadPriority;
	}

	public int getWarriorThreadPriority() {
		return warriorThreadPriority;
	}

	public int getWorkerThreadPriority() {
		return workerThreadPriority;
	}

	public void setWorkerThreadPriority(int workerThreadPriority) {
		this.workerThreadPriority = workerThreadPriority;
	}

	public int getMainThreadPriority() {
		return mainThreadPriority;
	}

	public void setMainThreadPriority(int mainThreadPriority) {
		this.mainThreadPriority = mainThreadPriority;
	}

}
