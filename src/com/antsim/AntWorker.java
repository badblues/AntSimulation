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
        ImageView antWorkerImageView = new ImageView(new Image("resources/ant_worker.png"));
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
        root.getChildren().add(antWorkerImageView);
    }
}
