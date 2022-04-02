package com.antsim;

import javafx.scene.*;
import javafx.scene.image.*;

public class AntWarrior extends Ant implements IBehavior {

	final ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
	Group root;

	AntWarrior() {
		super();
	}

	@Override
	void spawn(Group root, int time, int lifeT, int id) {
		System.out.println("Warrior spawned");
		antWarriorImageView.setX(posX);
		antWarriorImageView.setY(posY);
		root.getChildren().add(antWarriorImageView);
		this.root = root;

		spawnTime = time;
		lifeTime = lifeT;
		this.id = id;
	}

	protected void destroyImage() {
		root.getChildren().remove(antWarriorImageView);
	}
}
