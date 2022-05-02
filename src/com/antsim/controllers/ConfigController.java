package com.antsim.controllers;

import com.antsim.model.Habitat;

import java.io.*;
import java.util.Properties;

public class ConfigController {

	private Habitat model = Habitat.getInstance();
	private String fpath = "src/resources/data/config.properties";

	public void load() {
		try {
			Properties props = new Properties();
			FileReader reader = new FileReader(fpath);
			props.load(reader);
			loadValues(props);
			reader.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void save() {
		try {
			Properties props = new Properties();
			FileWriter writer = new FileWriter(fpath);
			props.setProperty("warrior_spawn_chance", Integer.toString(model.getWarriorSpawnChance()));
			props.setProperty("worker_spawn_chance", Integer.toString(model.getWorkerSpawnChance()));
			props.setProperty("warrior_spawn_delay", Integer.toString(model.getWarriorSpawnDelay()));
			props.setProperty("worker_spawn_delay", Integer.toString(model.getWorkerSpawnDelay()));
			props.setProperty("warrior_lifetime", Integer.toString(model.getWarriorLifeTime()));
			props.setProperty("worker_lifetime", Integer.toString(model.getWorkerLifeTime()));
			props.setProperty("warriors_TP", Integer.toString(model.getWarriorThreadPriority()));
			props.setProperty("workers_TP", Integer.toString(model.getWorkerThreadPriority()));
			props.setProperty("main_TP", Integer.toString(model.getMainThreadPriority()));
			props.store(writer, "Configuration");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void loadValues(Properties props) {
		int val;
		val = Integer.parseInt(props.getProperty("warrior_spawn_chance", "100"));
		if (val >= 0 && val <= 100 && val % 10 == 0)
			model.setWarriorSpawnChance(val);
		val = Integer.parseInt(props.getProperty("worker_spawn_chance", "100"));
		if (val >= 0 && val <= 100 && val % 10 == 0)
			model.setWorkerSpawnChance(val);
		val = Integer.parseInt(props.getProperty("warrior_spawn_delay", "1"));
		if (val >= Habitat.MIN_SPAWN_DELAY && val <= Habitat.MAX_SPAWN_DELAY)
			model.setWarriorSpawnDelay(val);
		val = Integer.parseInt(props.getProperty("worker_spawn_delay", "1"));
		if (val >= Habitat.MIN_SPAWN_DELAY && val <= Habitat.MAX_SPAWN_DELAY)
			model.setWorkerSpawnDelay(val);
		val = Integer.parseInt(props.getProperty("warrior_lifetime", "10"));
		if (val >= Habitat.MIN_LIFE_TIME && val <= Habitat.MAX_LIFE_TIME)
			model.setWarriorLifeTime(val);
		val = Integer.parseInt(props.getProperty("worker_lifetime", "10"));
		if (val >= Habitat.MIN_LIFE_TIME && val <= Habitat.MAX_LIFE_TIME)
			model.setWorkerLifeTime(val);
		val = Integer.parseInt(props.getProperty("warriors_TP", "5"));
		if (val >= Thread.MIN_PRIORITY && val <= Thread.MAX_PRIORITY)
			model.setWarriorThreadPriority(val);
		val = Integer.parseInt(props.getProperty("workers_TP", "5"));
		if (val >= Thread.MIN_PRIORITY && val <= Thread.MAX_PRIORITY)
			model.setWorkerThreadPriority(val);
		val = Integer.parseInt(props.getProperty("main_TP", "5"));
		if (val >= Thread.MIN_PRIORITY && val <= Thread.MAX_PRIORITY)
			model.setMainThreadPriority(val);
	}
}
