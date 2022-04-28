package com.antsim.ant;

import com.antsim.IBehavior;
import javafx.scene.*;
import javafx.scene.image.*;

import java.util.Random;

public class AntWarrior extends Ant implements IBehavior {
	final ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
	Group root;
	double movementAngle = 0;
	public static double movementRadius = 30;
	int movementDirection;

	public AntWarrior() {
		super();
		if (new Random().nextBoolean())
			movementDirection = 1;
		else
			movementDirection = -1;
	}

	public AntWarrior(int posX, int posY, int spawnX, int spawnY, int movementDirection, double movementAngle) {
		super(posX, posY, spawnX, spawnY);
		this.movementDirection = movementDirection;
		this.movementAngle = movementAngle;
	}

	@Override
	public void spawn(Group root, int time, int lifeT, int id) {
		antWarriorImageView.setX(posX);
		antWarriorImageView.setY(posY);
		root.getChildren().add(antWarriorImageView);
		this.root = root;
		spawnTime = time; 
		lifeTime = lifeT;
		this.id = id;
	}

	public void moveImage() {
		if (posX > 40 && posX < 760)
			antWarriorImageView.setX(posX);
		if (posY > 20 && posY < 660)
			antWarriorImageView.setY(posY);
		if (movementDirection == 1)
			antWarriorImageView.setRotate(movementAngle * 180/Math.PI + 180);
		else
			antWarriorImageView.setRotate(movementAngle * 180/Math.PI);
	}

	public void destroyAnt() {
		root.getChildren().remove(antWarriorImageView);
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
