package com.antsim;

import javafx.scene.Group;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class AntWarrior extends Ant implements IBehavior {

    final ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
    Group root;

    AntWarrior() {
        super();
    }

    @Override
    void spawn(Group root, int time, int lifeT, int id) {
        antWarriorImageView.setX(posX);
        antWarriorImageView.setY(posY);
        root.getChildren().add(antWarriorImageView);
        this.root = root;

        spawnTime = time;
        Random rand = new Random();
        lifeTime = lifeT;
        this.id = id;
    }

    protected void destroyImage() {
        root.getChildren().remove(antWarriorImageView);
    }
}
