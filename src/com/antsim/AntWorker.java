package com.antsim;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWorker extends Ant implements IBehavior{

    AntWorker() {
        super();
    }
    @Override
    void spawn(Group root) {
        Image antWorker = new Image("resources/ant_worker.png");
        ImageView antWorkerImageView = new ImageView(antWorker);
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
    }
}
