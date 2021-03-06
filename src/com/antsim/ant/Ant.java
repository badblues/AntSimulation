package com.antsim.ant;

import com.antsim.model.Habitat;
import javafx.scene.*;

import java.io.*;
import java.util.*;


public abstract class Ant implements IBehavior, Serializable {
	int posX;
	int posY;
	int spawnX;
	int spawnY;
	int spawnTime;
	int lifeTime;
	int id;

	Ant(int posX, int posY, int spawnX, int spawnY) {
		this.posX = posX;
		this.posY = posY;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
	}
	Ant() {
		Random rand = new Random();
		posX = spawnX = rand.nextInt(760);
		posY = spawnY = rand.nextInt(660) + 20;
	}

	@Serial
	private void writeObject(ObjectOutputStream out) throws IOException {
		lifeTime -= Habitat.getInstance().getTime() - spawnTime;  //making that ant dies in right time after loading
		out.defaultWriteObject();
	}

	@Serial
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	public abstract void spawn(Group root, int time, int lifeT, int id);

	public abstract void destroyAnt();

	public abstract void moveImage();

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

	public int getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
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