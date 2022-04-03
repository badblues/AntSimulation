package com.antsim.ant;

import com.antsim.IBehavior;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWorker extends Ant implements IBehavior {
    final ImageView antWorkerImageView = new ImageView(new Image("resources/ant_worker.png"));
    Group root;

    public AntWorker() {
        super();
    }

    @Override
    public void spawn(Group root, int time, int lifeT ,int id) {
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
        this.root = root;

        spawnTime = time;
        lifeTime = lifeT;
        this.id = id;
    }

    public void destroyImage() {
        root.getChildren().remove(antWorkerImageView);
    }

}
