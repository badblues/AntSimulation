package com.antsim.ant;

import com.antsim.IBehavior;
import com.antsim.ai.AntWorkerAI;
import com.antsim.ai.Destination;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWorker extends Ant implements IBehavior {
    final ImageView antWorkerImageView = new ImageView(new Image("resources/ant_worker.png"));
    Group root;
    Destination destination = Destination.HOME;

    public AntWorker(int posX, int posY, int spawnX, int spawnY, int destination) {
        super(posX, posY, spawnX, spawnY);
        this.destination = Destination.values()[destination];
    }

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

    public void moveImage() {
        antWorkerImageView.setX(posX);
        antWorkerImageView.setY(posY);
    }

    public void destroyAnt() {
        root.getChildren().remove(antWorkerImageView);
    }


    public void changeDestination() {
        if (destination == Destination.HOME)
            destination = Destination.SPAWN;
        else
            destination = Destination.HOME;
    }

    public Destination getDestination() {
        return destination;
    }

}
