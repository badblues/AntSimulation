package com.antsim;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;
import com.antsim.ant.AntWorker;
import javafx.scene.Group;

import java.io.*;

/* SAVING/LOADING ORDER:
    Ant
        type
        posX
        posY
        spawnX
        spawnY
        lifeTime
        id

    AntWarrior:
        movementAngle
        movementDirection

    AntWorker
        destination
*/

public class SaverController {
    private static Habitat model = Habitat.getInstance();
    private static String fpath = "src/com/antsim/config/ants.bin";

    public static void save() {
        try(DataOutputStream out = new DataOutputStream(new FileOutputStream(fpath, false))) {
            synchronized (model.getAntsVector()) {
                for(Ant ant: model.getAntsVector()) {
                    if (ant instanceof AntWarrior)
                        saveAntWarrior(out, (AntWarrior) ant);
                    else
                        saveAntWorker(out, (AntWorker) ant);
                }
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveAntWarrior(DataOutputStream out, AntWarrior ant) throws IOException {
        out.writeInt(1);
        saveAnt(out, ant);
        out.writeDouble(ant.getMovementAngle());
        out.writeInt(ant.getMovementDirection());
    }

    private static void saveAntWorker(DataOutputStream out, AntWorker ant) throws IOException {
        out.writeInt(0);
        saveAnt(out, ant);
        out.writeInt(ant.getDestination().ordinal());
    }

    private static void saveAnt(DataOutputStream out, Ant ant) throws IOException {
        out.writeInt(ant.getPosX());
        out.writeInt(ant.getPosY());
        out.writeInt(ant.getSpawnX());
        out.writeInt(ant.getSpawnY());
        out.writeInt(ant.getLifeTime() - (model.getTime() - ant.getSpawnTime()));  //lifeTime
        out.writeInt(ant.getId());
    }

    public static void load(Group antsArea) {
        try(DataInputStream in = new DataInputStream(new FileInputStream(fpath))) {
            synchronized (model.getAntsVector()) {
                while(in.available() > 0) {
                    if (in.readInt() == 1)
                        loadAntWarrior(in, antsArea);
                    else
                        loadAntWorker(in, antsArea);
                }
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadAntWarrior(DataInputStream in, Group antsArea) throws IOException {
        int posX = in.readInt();
        int posY = in.readInt();
        int spawnX = in.readInt();
        int spawnY = in.readInt();
        int lifeTime = in.readInt();
        int id = in.readInt();
        double movementAngle = in.readDouble();
        int movementDirection = in.readInt();
        AntWarrior a = new AntWarrior(posX, posY, spawnX, spawnY, movementDirection, movementAngle);
        a.spawn(antsArea, model.getTime(), lifeTime, id);
        model.getAntsVector().add(a);
        model.getAntsIdsHashSet().add(id);
        model.getAntsSpawnTimeTree().put(id, model.getTime());
        model.setAntWarriorCount(model.getAntWarriorCount() + 1);
    }

    private static void loadAntWorker(DataInputStream in, Group antsArea) throws IOException {
        int posX = in.readInt();
        int posY = in.readInt();
        int spawnX = in.readInt();
        int spawnY = in.readInt();
        int lifeTime = in.readInt();
        int id = in.readInt();
        int destination = in.readInt();
        AntWorker a = new AntWorker(posX, posY, spawnX, spawnY, destination);
        a.spawn(antsArea, model.getTime(), lifeTime, id);
        model.getAntsVector().add(a);
        model.getAntsIdsHashSet().add(id);
        model.getAntsSpawnTimeTree().put(id, model.getTime());
        model.setAntWarriorCount(model.getAntWorkerCount() + 1);
    }

}
