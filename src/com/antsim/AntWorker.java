package com.antsim;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class AntWorker extends Ant implements IBehavior{
    final ImageView antWorkerImageView = new ImageView(new Image("resources/ant_worker.png"));
    Group root;

    AntWorker() {
        super();
    }
    @Override
    void spawn(Group root, int time, int lifeT ,int id) {
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
        this.root = root;

        spawnTime = time;
        Random rand = new Random();
        lifeTime = lifeT;
    }

    protected void destroyImage() {
        root.getChildren().remove(antWorkerImageView);
    }

}
