package com.antsim;

import javafx.scene.*;

import java.util.*;

public abstract class Ant implements IBehavior {
	int posX;
	int posY;
	int spawnTime;
	int lifeTime;
	int id;

	Ant() {
		Random rand = new Random();
		posX = rand.nextInt(760);
		posY = rand.nextInt(680);
		id = rand.nextInt(1000);
	}

	abstract void spawn(Group root, int time, int lifeT, int id);

	abstract void destroyImage();

	int getId() {
		return id;
	}

	int getPosX() {
		return posX;
	}

	void setPosX(int x) {
		posX = x;
	}

	int getPosY() {
		return posY;
	}

	void setPosY(int y) {
		posY = y;
	}

	int getSpawnTime() {
		return spawnTime;
	}

	int getLifeTime() {
		return lifeTime;
	}

	void setLifeTime(int time) {
		spawnTime = time;
	}
}