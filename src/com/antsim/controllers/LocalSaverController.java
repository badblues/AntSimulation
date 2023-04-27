package com.antsim.controllers;

import com.antsim.ant.Ant;
import com.antsim.ant.Warrior;
import com.antsim.model.Habitat;
import javafx.scene.Group;
import javafx.stage.FileChooser;

import java.io.*;

public class LocalSaverController {
    private final Habitat model = Habitat.getInstance();

    public void saveAll() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setInitialFileName("ants.bin");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Bin files", "*.bin"));
        File file = fileChooser.showSaveDialog(Habitat.getInstance().getStage());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false))) {
            synchronized (model.getAntsVector()) {
                for(Ant ant: model.getAntsVector()) {
                    out.writeObject(ant);
                }
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadAll(Group antsArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Bin files", "*.bin"));
        File file = fileChooser.showSaveDialog(Habitat.getInstance().getStage());
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            synchronized (model.getAntsVector()) {
                while(true) {
                    try {
                        Ant ant = (Ant) in.readObject();
                        ant.spawn(antsArea, model.getTime(), ant.getLifeTime(), ant.getId());
                        model.getAntsVector().add(ant);
                        model.getAntsIdsHashSet().add(ant.getId());
                        model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                        if (ant instanceof Warrior)
                            model.increaseWarriorCount(1);
                        else
                            model.increaseWorkerCount(1);
                    } catch(EOFException ex) {
                        break;
                    }
                }
                in.close();
            }
        } catch (IOException ex) {

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
