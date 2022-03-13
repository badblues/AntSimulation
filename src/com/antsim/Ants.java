package com.antsim;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.util.Random;

public abstract class Ants extends Group implements IBehavior {
    int posX;
    int posY;

    Ants() {
        Random rand = new Random();
        posX = rand.nextInt(520);
        posY = rand.nextInt(520);
    }

    abstract void spawn(Group root);
}