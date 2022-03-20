package com.antsim;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AntWarrior extends Ant implements IBehavior {

    final ImageView antWarriorImageView = new ImageView(new Image("resources/ant_warrior.png"));
    Group root;

    AntWarrior() {
        super();
    }

    @Override
    void spawn(Group root) {
        antWarriorImageView.setX(posX);
        antWarriorImageView.setY(posY);
        root.getChildren().add(antWarriorImageView);
        this.root = root;
    }

    protected void destroyImage() {
        root.getChildren().remove(antWarriorImageView);
    }
}
