package com.antsim.ant;

import javafx.scene.*;
import javafx.scene.image.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Random;

public class Warrior extends Ant implements IBehavior {
	transient ImageView warriorImageView = new ImageView(new Image("resources/images/ant_warrior.png"));
	transient Group root;
	double movementAngle = 0;
	public static final double MOVEMENT_RADIUS = 30;
	int movementDirection;

	public Warrior() {
		super();
		if (new Random().nextBoolean())
			movementDirection = 1;
		else
			movementDirection = -1;
	}

	public Warrior(int posX, int posY, int spawnX, int spawnY, int movementDirection, double movementAngle) {
		super(posX, posY, spawnX, spawnY);
		this.movementDirection = movementDirection;
		this.movementAngle = movementAngle;
	}

	@Override
	public void spawn(Group root, int time, int lifeT, int id) {
		warriorImageView.setX(posX);
		warriorImageView.setY(posY);
		root.getChildren().add(warriorImageView);
		this.root = root;
		spawnTime = time; 
		lifeTime = lifeT;
		this.id = id;
	}

	public void moveImage() {
		if (posX > 40 && posX < 760)
			warriorImageView.setX(posX);
		if (posY > 20 && posY < 660)
			warriorImageView.setY(posY);
		if (movementDirection == 1)
			warriorImageView.setRotate(movementAngle * 180/Math.PI + 180);
		else
			warriorImageView.setRotate(movementAngle * 180/Math.PI);
	}

	@Serial
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	@Serial
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.warriorImageView = new ImageView(new Image("resources/images/ant_warrior.png"));
	}

	public void destroyAnt() {
		root.getChildren().remove(warriorImageView);
	}

	public void setMovementAngle(double angle) {
		movementAngle = angle;
	}

	public double getMovementAngle() {
		return movementAngle;
	}

	public int getMovementDirection() {
		return movementDirection;
	}

}
