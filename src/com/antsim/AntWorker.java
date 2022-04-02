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
        System.out.println("Worker spawned");
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
        this.root = root;

        spawnTime = time;
        lifeTime = lifeT;
        this.id = id;
    }

    protected void destroyImage() {
        root.getChildren().remove(antWorkerImageView);
    }

}
