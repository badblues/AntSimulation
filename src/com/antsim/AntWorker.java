package com.antsim;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWorker extends Ant implements IBehavior{
    final ImageView antWorkerImageView = new ImageView(new Image("resources/ant_worker.png"));
    Group root;

    AntWorker() {
        super();
    }
    @Override
    void spawn(Group root) {
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
        this.root = root;
    }

    protected void destroyImage() {
        root.getChildren().remove(antWorkerImageView);
    }

}
