package com.antsim;

import javafx.scene.Group;

import java.util.Random;

public abstract class Ant implements IBehavior {
    int posX;
    int posY;
    int spawnTime;
    int lifeTime;

    Ant() {
        Random rand = new Random();
        posX = rand.nextInt(500) + 200;
        posY = rand.nextInt(460) + 40;
    }

    abstract void spawn(Group root, int time);
    abstract void destroyImage();
}