package com.antsim;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public class Model {

    private static Model instance;

    private Vector<Ant> antsVector = new Vector<>();
    private HashSet<Integer> antsIds = new HashSet<>();
    private TreeMap<Integer, Integer> antsSpawnTime = new TreeMap<>();
    private int antWarriorSpawnChance = 100; //percents
    private int antWorkerSpawnChance = 100; //percents
    private int antWarriorSpawnDelay = 1; // seconds
    private int antWorkerSpawnDelay = 1; // seconds
    static final int MAX_SPAWN_DELAY = 15;
    static final int MIN_SPAWN_DELAY = 1;
    static final int MIN_LIFE_TIME = 1;
    static final int MAX_LIFE_TIME = 30;
    private int antWarriorCount;
    private int antWorkerCount;
    private int time;
    private int timeToAntWarriorSpawn = antWarriorSpawnDelay;
    private int timeToAntWorkerSpawn = antWorkerSpawnDelay;
    private int antWarriorLifeTime = 10;
    private int antWorkerLifeTime = 10;

    private static synchronized Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }

    Vector<Ant> getAntsVector() {
        return antsVector;
    }

    HashSet<Integer> getAntsIds() { return antsIds; }

    TreeMap<Integer, Integer> getAntsSpawnTime() { return antsSpawnTime; }

    int getAntWarriorSpawnChance() {
        return antWarriorSpawnChance;
    }

    int getAntWorkerSpawnChance() {
        return antWorkerSpawnChance;
    }

    int getAntWarriorSpawnDelay() {
        return antWarriorSpawnDelay;
    }

    int getAntWorkerSpawnDelay() { return antWorkerSpawnDelay; }

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

    int getAntWarriorLifeTime() { return antWarriorLifeTime; }

    int getAntWorkerLifeTime() { return antWorkerLifeTime; }

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

    void setTimeToAntWarriorSpawn(int time) { timeToAntWarriorSpawn = time; }

    void setTimeToAntWorkerSpawn(int time) { timeToAntWorkerSpawn = time; }

    void setAntWarriorLifeTime(int time) { antWarriorLifeTime = time; }

    void setAntWorkerLifeTime(int time) { antWorkerLifeTime = time; }

}
