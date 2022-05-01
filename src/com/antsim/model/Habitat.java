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
	private final Vector<Ant> antsVector = new Vector<>();
	private final HashSet<Integer> antsIds = new HashSet<>();
	private final TreeMap<Integer, Integer> antsSpawnTime = new TreeMap<>();
	private int antWarriorSpawnChance = 100; //percents
	private int antWorkerSpawnChance = 100; //percents
	private int antWarriorSpawnDelay = 1; // seconds
	private int antWorkerSpawnDelay = 1; // seconds
	private int antWarriorCount;
	private int antWorkerCount;
	private int time;
	private int timeToAntWarriorSpawn = antWarriorSpawnDelay;
	private int timeToAntWorkerSpawn = antWorkerSpawnDelay;
	private int antWarriorLifeTime = 10;
	private int antWorkerLifeTime = 10;
	private int antWarriorThreadPriority = 5;
	private int antWorkerThreadPriority = 5;
	private int mainThreadPriority = 5;

	public static synchronized Habitat getInstance() {
		if(instance == null)
			instance = new Habitat();
		return instance;
	}

	public static void main(String[] args) {
		ConfigController.load();
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
				ConfigController.save();
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

	public int getAntWarriorSpawnChance() {
		return antWarriorSpawnChance;
	}

	public void setAntWarriorSpawnChance(int chance) {
		antWarriorSpawnChance = chance;
	}

	public int getAntWorkerSpawnChance() {
		return antWorkerSpawnChance;
	}

	public void setAntWorkerSpawnChance(int chance) {
		antWorkerSpawnChance = chance;
	}

	public int getAntWarriorSpawnDelay() {
		return antWarriorSpawnDelay;
	}

	public void setAntWarriorSpawnDelay(int delay) {
		antWarriorSpawnDelay = delay;
	}

	public int getAntWorkerSpawnDelay() {return antWorkerSpawnDelay;}

	public void setAntWorkerSpawnDelay(int delay) {
		antWorkerSpawnDelay = delay;
	}

	public int getAntWarriorCount() {
		return antWarriorCount;
	}

	public void setAntWarriorCount(int count) {
		antWarriorCount = count;
	}

	public int getAntWorkerCount() {
		return antWorkerCount;
	}

	public void setAntWorkerCount(int count) {
		antWorkerCount = count;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTimeToAntWarriorSpawn() {
		return timeToAntWarriorSpawn;
	}

	public void setTimeToAntWarriorSpawn(int time) {timeToAntWarriorSpawn = time;}

	public int getTimeToAntWorkerSpawn() {
		return timeToAntWorkerSpawn;
	}

	public void setTimeToAntWorkerSpawn(int time) {
        timeToAntWorkerSpawn = time;
    }

	public int getAntWarriorLifeTime() {
        return antWarriorLifeTime;
    }

	public void setAntWarriorLifeTime(int time) {
        antWarriorLifeTime = time;
    }

	public int getAntWorkerLifeTime() {
        return antWorkerLifeTime;
    }

	public void setAntWorkerLifeTime(int time) {
        antWorkerLifeTime = time;
    }

	public void setAntWarriorThreadPriority(int antWarriorThreadPriority) {
		this.antWarriorThreadPriority = antWarriorThreadPriority;
	}

	public int getAntWarriorThreadPriority() {
		return antWarriorThreadPriority;
	}

	public int getAntWorkerThreadPriority() {
		return antWorkerThreadPriority;
	}

	public void setAntWorkerThreadPriority(int antWorkerThreadPriority) {
		this.antWorkerThreadPriority = antWorkerThreadPriority;
	}

	public int getMainThreadPriority() {
		return mainThreadPriority;
	}

	public void setMainThreadPriority(int mainThreadPriority) {
		this.mainThreadPriority = mainThreadPriority;
	}

}
