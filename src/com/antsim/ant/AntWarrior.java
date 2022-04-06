package com.antsim.ant;

import com.antsim.IBehavior;
import com.antsim.ai.AntWarriorAI;
import javafx.scene.*;
import javafx.scene.image.*;

public class AntWarrior extends Ant implements IBehavior {

	final ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
	Group root;
	AntWarriorAI antWarriorAI;


	public AntWarrior() {
		super();
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
		antWarriorAI = new AntWarriorAI(this);
		antWarriorAI.start();
	}

	public void moveImage(int newX, int newY) {
		antWarriorImageView.setX(newX);
		antWarriorImageView.setY(newY);
	}

	public void destroyAnt() {
		antWarriorAI.kill();
		root.getChildren().remove(antWarriorImageView);
	}



}
