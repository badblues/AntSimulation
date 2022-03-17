package com.antsim;

import javafx.scene.Group;

import java.util.Random;

public abstract class Ant implements IBehavior {
    int posX;
    int posY;

    Ant() {
        Random rand = new Random();
        posX = rand.nextInt(500) + 200;
        posY = rand.nextInt(500);
    }

    abstract void spawn(Group root);
}