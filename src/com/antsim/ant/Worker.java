package com.antsim.ant;

import com.antsim.ai.Destination;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class Worker extends Ant implements IBehavior {
    transient ImageView workerImageView = new ImageView(new Image("resources/images/ant_worker.png"));
    transient Group root;
    Destination destination = Destination.HOME;

    public Worker(int posX, int posY, int spawnX, int spawnY, int destination) {
        super(posX, posY, spawnX, spawnY);
        this.destination = Destination.values()[destination];
    }

    public Worker() {
        super();
    }

    @Override
    public void spawn(Group root, int time, int lifeT ,int id) {
        workerImageView.setX(posX);
        workerImageView.setY(posY);
        root.getChildren().add(workerImageView);
        this.root = root;

        spawnTime = time;
        lifeTime = lifeT;
        this.id = id;
    }

    public void moveImage() {
        workerImageView.setX(posX);
        workerImageView.setY(posY);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.workerImageView = new ImageView(new Image("resources/images/ant_worker.png"));
    }

    public void destroyAnt() {
        root.getChildren().remove(workerImageView);
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
