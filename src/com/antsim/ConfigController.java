package com.antsim;

import java.io.*;
import java.util.Scanner;

//TODO recreate using BufferedReader

public class ConfigController {

	private static Habitat model = Habitat.getInstance();

	static public void load() {
		boolean error = false;
		try {
			Scanner scanner = new Scanner(new File("src/com/antsim/config/conf.txt"));
			if (!(readSpawnChance(scanner) && readSpawnDelay(scanner) && readLifetime(scanner)))
				setDefaultConf();
			else
				System.out.println("zaebis");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			error = true;
		}
		if (error)
			setDefaultConf();
	}

	static public void save() {

	}

	private static boolean readSpawnChance(Scanner scanner) {
		boolean noError = true;
		int val;
		if (scanner.hasNextInt()) {
			val = scanner.nextInt();
			if (val >= 10 && val <= 100 && val % 10 == 0) {
				model.setAntWarriorSpawnChance(val);
				scanner.skip("= AntWarrior spawn chance(1, 100)");
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= 10 && val <= 100 && val % 10 == 0) {
				model.setAntWorkerSpawnChance(val);
				scanner.skip("=AntWorker spawn chance(1, 100)");
			} else noError = false;
		} else noError = false;
		return noError;
	}

	private static boolean readSpawnDelay(Scanner scanner) {
		boolean noError = true;
		int val;
		if (scanner.hasNextInt()) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_SPAWN_DELAY && val <= Habitat.MAX_SPAWN_DELAY) {
				model.setAntWarriorSpawnDelay(val);
				scanner.skip("= AntWarrior spawn delay(1, 30)\n");
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_SPAWN_DELAY && val <= Habitat.MAX_SPAWN_DELAY) {
				model.setAntWorkerSpawnDelay(val);
				scanner.skip("= AntWorker spawn delay(1, 30)\n");
			} else noError = false;
		} else noError = false;
		return noError;
	}

	private static boolean readLifetime(Scanner scanner) {
		boolean noError = true;
		int val;
		if (scanner.hasNextInt()) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_LIFE_TIME && val <= Habitat.MAX_LIFE_TIME) {
				model.setAntWarriorLifeTime(val);
				scanner.skip("= AntWarrior lifetime(1, 20)\n");
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_LIFE_TIME && val <= Habitat.MAX_LIFE_TIME) {
				model.setAntWorkerLifeTime(val);
				scanner.skip("= AntWorker lifetime(1, 20)\n");
			} else noError = false;
		} else noError = false;
		return noError;
	}

	private static void setDefaultConf() {
		model.setAntWarriorSpawnChance(100);
		model.setAntWorkerSpawnChance(100);
		model.setAntWarriorSpawnDelay(1);
		model.setAntWorkerSpawnDelay(1);
		model.setAntWarriorLifeTime(10);
		model.setAntWorkerLifeTime(10);
	}
}
