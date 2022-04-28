package com.antsim;

import java.io.*;
import java.util.Scanner;

//TODO recreate using BufferedReader

public class ConfigController {

	private static Habitat model = Habitat.getInstance();
	private static String fpath = "src/com/antsim/config/conf.txt";

	static public void load() {
		try {
			Scanner scanner = new Scanner(new File(fpath));
			if (!(readSpawnChance(scanner) && readSpawnDelay(scanner) && readLifetime(scanner))) {
				setDefaultConf();
				System.out.println("Configuration file was damaged");
			}
		} catch(FileNotFoundException e) {
			setDefaultConf();
			System.out.println("Configuration file wasn't founded\n");
		}
	}

	static public void save() {
		try {
			new File(fpath).createNewFile();
			FileWriter writer = new FileWriter(fpath, false);
			writeValues(writer);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean readSpawnChance(Scanner scanner) {
		boolean noError = true;
		int val;
		if (scanner.hasNextInt()) {
			val = scanner.nextInt();
			if (val >= 0 && val <= 100 && val % 10 == 0) {
				model.setAntWarriorSpawnChance(val);
				scanner.nextLine();
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= 0 && val <= 100 && val % 10 == 0) {
				model.setAntWorkerSpawnChance(val);
				scanner.nextLine();
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
				scanner.nextLine();
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_SPAWN_DELAY && val <= Habitat.MAX_SPAWN_DELAY) {
				model.setAntWorkerSpawnDelay(val);
				scanner.nextLine();
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
				scanner.nextLine();
			} else noError = false;
		} else noError = false;
		if (scanner.hasNextInt() && noError) {
			val = scanner.nextInt();
			if (val >= Habitat.MIN_LIFE_TIME && val <= Habitat.MAX_LIFE_TIME) {
				model.setAntWorkerLifeTime(val);
			} else noError = false;
		} else noError = false;
		return noError;
	}

	private static void writeValues(FileWriter writer) throws IOException {
		writer.write(model.getAntWarriorSpawnChance() + " = AntWarrior spawn chance(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)\n");
		writer.write(model.getAntWorkerSpawnChance() + " = AntWorker spawn chance(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)\n");
		writer.write(model.getAntWarriorSpawnDelay() + " = AntWarrior spawn delay(1, 30)\n");
		writer.write(model.getAntWorkerSpawnDelay() + " = AntWorker spawn delay(1, 30)\n");
		writer.write(model.getAntWarriorLifeTime() + " = AntWarrior lifetime(1, 20)\n");
		writer.write(model.getAntWorkerLifeTime() + " = AntWorker lifetime(1, 20)");
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
