package com.antsim;

import javafx.scene.Group;

import java.util.Random;

public abstract class Ant implements IBehavior {
    int posX;
    int posY;

    Ant() {
        Random rand = new Random();
        posX = rand.nextInt(520);
        posY = rand.nextInt(520);
    }
    abstract void spawn(Group root);
}