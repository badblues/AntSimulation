package com.antsim.ant;

import com.antsim.ai.Destination;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class AntWorker extends Ant implements IBehavior {
    transient ImageView antWorkerImageView = new ImageView(new Image("resources/images/ant_worker.png"));
    transient Group root;
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.antWorkerImageView = new ImageView(new Image("resources/images/ant_worker.png"));
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
