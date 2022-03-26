package com.antsim;

import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;

public class Model {

    private static Model instance;

    private ArrayList<Ant> antsArray = new ArrayList<Ant>();
    private int antWarriorSpawnChance = 100; //percents
    private int antWorkerSpawnChance = 100; //percents
    private     int antWarriorSpawnDelay = 1; // seconds
    private int antWorkerSpawnDelay = 1; // seconds
    static final int MAX_SPAWN_DELAY = 15;
    static final int MIN_SPAWN_DELAY = 1;
    private int antWarriorCount;
    private int antWorkerCount;
    private int time;
    private int timeToAntWarriorSpawn = antWarriorSpawnDelay;
    private int timeToAntWorkerSpawn = antWorkerSpawnDelay;

    private static synchronized Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }

    ArrayList<Ant> getAntsArray() {
        return antsArray;
    }

    int getAntWarriorSpawnChance() {
        return antWarriorSpawnChance;
    }

    int getAntWorkerSpawnChance() {
        return antWorkerSpawnChance;
    }

    int getAntWarriorSpawnDelay() {
        return antWarriorSpawnDelay;
    }

    int getAntWorkerSpawnDelay() {
        return antWorkerSpawnDelay;
    }

    int getAntWarriorCount() {
        return antWarriorCount;
    }

    int getAntWorkerCount() {
        return antWorkerCount;
    }

    int getTime() {
        return time;
    }

    int getTimeToAntWarriorSpawn() {
        return timeToAntWarriorSpawn;
    }

    int getTimeToAntWorkerSpawn() {
        return timeToAntWorkerSpawn;
    }

    void setAntWarriorSpawnChance(int chance) {
        antWarriorSpawnChance = chance;
    }

    void setAntWorkerSpawnChance(int chance) {
        antWorkerSpawnChance = chance;
    }

    void setAntWarriorSpawnDelay(int delay) {
        antWarriorSpawnDelay = delay;
    }

    void setAntWorkerSpawnDelay(int delay) {
        antWorkerSpawnDelay = delay;
    }

    void setAntWarriorCount(int count) {
        antWarriorCount = count;
    }

    void setAntWorkerCount(int count) {
        antWorkerCount = count;
    }

    void setTime(int time) {
        this.time = time;
    }

    void setTimeToAntWarriorSpawn(int time) {
        timeToAntWarriorSpawn = time;
    }

    void setTimeToAntWorkerSpawn(int time) {
        timeToAntWorkerSpawn = time;
    }

}
