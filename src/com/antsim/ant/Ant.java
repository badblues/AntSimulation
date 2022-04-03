package com.antsim.ant;

import com.antsim.IBehavior;
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
		posY = rand.nextInt(660) + 20;
		id = rand.nextInt(1000);
	}

	public abstract void spawn(Group root, int time, int lifeT, int id);

	public abstract void destroyImage();

	public int getId() {
		return id;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int x) {
		posX = x;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int y) {
		posY = y;
	}

	public int getSpawnTime() {
		return spawnTime;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int time) {
		spawnTime = time;
	}
}