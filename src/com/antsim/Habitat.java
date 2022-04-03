package com.antsim;

import com.antsim.ant.*;

import javafx.application.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

import java.util.*;

//TODO ключ тримапы - класс АНТ
//TODO поиск в дереве по value и удаление по Id
//TODO configure different packages

public class Habitat extends Application {

	static final int MAX_SPAWN_DELAY = 30;
	static final int MIN_SPAWN_DELAY = 1;
	static final int MIN_LIFE_TIME = 1;
	static final int MAX_LIFE_TIME = 30;
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

	static synchronized Habitat getInstance() {
		if(instance == null)
			instance = new Habitat();
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("AntSim.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	Vector<Ant> getAntsVector() {
		return antsVector;
	}

	HashSet<Integer> getAntsIdsHashSet() {
        return antsIds;
    }

	TreeMap<Integer, Integer> getAntsSpawnTimeTree() {
        return antsSpawnTime;
    }

	int getAntWarriorSpawnChance() {
		return antWarriorSpawnChance;
	}

	void setAntWarriorSpawnChance(int chance) {
		antWarriorSpawnChance = chance;
	}

	int getAntWorkerSpawnChance() {
		return antWorkerSpawnChance;
	}

	void setAntWorkerSpawnChance(int chance) {
		antWorkerSpawnChance = chance;
	}

	int getAntWarriorSpawnDelay() {
		return antWarriorSpawnDelay;
	}

	void setAntWarriorSpawnDelay(int delay) {
		antWarriorSpawnDelay = delay;
	}

	int getAntWorkerSpawnDelay() {return antWorkerSpawnDelay;}

	void setAntWorkerSpawnDelay(int delay) {
		antWorkerSpawnDelay = delay;
	}

	int getAntWarriorCount() {
		return antWarriorCount;
	}

	void setAntWarriorCount(int count) {
		antWarriorCount = count;
	}

	int getAntWorkerCount() {
		return antWorkerCount;
	}

	void setAntWorkerCount(int count) {
		antWorkerCount = count;
	}

	int getTime() {
		return time;
	}

	void setTime(int time) {
		this.time = time;
	}

	int getTimeToAntWarriorSpawn() {
		return timeToAntWarriorSpawn;
	}

	void setTimeToAntWarriorSpawn(int time) {timeToAntWarriorSpawn = time;}

	int getTimeToAntWorkerSpawn() {
		return timeToAntWorkerSpawn;
	}

	void setTimeToAntWorkerSpawn(int time) {
        timeToAntWorkerSpawn = time;
    }

	int getAntWarriorLifeTime() {
        return antWarriorLifeTime;
    }

	void setAntWarriorLifeTime(int time) {
        antWarriorLifeTime = time;
    }

	int getAntWorkerLifeTime() {
        return antWorkerLifeTime;
    }

	void setAntWorkerLifeTime(int time) {
        antWorkerLifeTime = time;
    }
}
