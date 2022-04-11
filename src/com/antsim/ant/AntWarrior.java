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
	}

	@Override
	public void spawn(Group root, int time, int lifeT, int id) {
		antWarriorImageView.setX(posX);
		antWarriorImageView.setY(posY);
		if (new Random().nextBoolean())
			movementDirection = 1;
		else
			movementDirection = -1;
		root.getChildren().add(antWarriorImageView);
		this.root = root;

		spawnTime = time;
		lifeTime = lifeT;
		this.id = id;
	}

	public void moveImage(int newX, int newY) {
		if (newX < 760)
			antWarriorImageView.setX(newX);
		if (newY > 20 && newY < 660)
			antWarriorImageView.setY(newY);
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
