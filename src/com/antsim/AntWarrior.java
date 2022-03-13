package com.antsim;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWarrior extends Ants implements IBehavior {
    AntWarrior() {
        super();
    }

    @Override
    void spawn(Group root) {
        Image antWarrior = new Image("resources/ant_warrior.png");
        ImageView antWarriorImageView = new ImageView(antWarrior);
        antWarriorImageView.setX(posX);
        antWarriorImageView.setY(posY);
        root.getChildren().add(antWarriorImageView);
    }
}
