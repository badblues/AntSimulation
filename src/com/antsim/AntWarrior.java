package com.antsim;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWarrior extends Ant implements IBehavior {

    AntWarrior() {
        super();
    }

    @Override
    void spawn(Group root) {
        ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
        antWarriorImageView.setX(posX);
        antWarriorImageView.setY(posY);
        root.getChildren().add(antWarriorImageView);
    }

}
