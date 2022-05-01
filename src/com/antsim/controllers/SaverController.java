package com.antsim.controllers;

import com.antsim.ant.Ant;
import com.antsim.model.Habitat;
import javafx.scene.Group;

import java.io.*;

public class SaverController {
    private static final Habitat model = Habitat.getInstance();
    private static final String fpath = "src/resources/data/ants.bin";

    public static void saveAll() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fpath, false))) {
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

    public static void loadAll(Group antsArea) {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fpath))) {
            synchronized (model.getAntsVector()) {
                while(true) {
                    try {
                        Ant ant = (Ant) in.readObject();
                        ant.spawn(antsArea, model.getTime(), ant.getLifeTime(), ant.getId());
                        model.getAntsVector().add(ant);
                        model.getAntsIdsHashSet().add(ant.getId());
                        model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                        model.setWarriorCount(model.getWarriorCount() + 1);
                    } catch(EOFException ex) {
                        break;
                    }
                }
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
