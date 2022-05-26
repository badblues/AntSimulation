package com.antsim.controllers;

import com.antsim.ai.Destination;
import com.antsim.ant.*;
import com.antsim.model.Habitat;
import com.antsim.server.AntsServer;
import javafx.application.Platform;
import javafx.scene.Group;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Vector;

public class ClientController extends Thread {

    Habitat model = Habitat.getInstance();
    private static Socket socket;
    private final SimulationController simulationController;
    private final AntRequestController antRequestController;
    Group antsArea;

    public ClientController(SimulationController controller, AntRequestController requestController, Group antsArea) {
        simulationController = controller;
        antRequestController = requestController;
        this.antsArea = antsArea;
        try {
            socket = new Socket("localhost", 4999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                InputStream inputStream = socket.getInputStream();
                DataInputStream din = new DataInputStream(inputStream);
                if (din.available() > 0) {
                    int i = din.readInt();
                    switch (i) {
                        case AntsServer.CODE_CLIENTS_UPDATE:
                            readClientsUpdate(din);
                            break;
                        case AntsServer.CODE_ANTS_REQUEST:
                            System.out.println("got ant request");
                            sendAnts(din);
                            break;
                        case AntsServer.CODE_ANTS_RESPONSE:
                            readAnts();
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendRequest(int id, int number) {
        try {
            System.out.println("sending request");
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            out.writeInt(AntsServer.CODE_ANTS_REQUEST);
            out.writeInt(id);
            out.writeInt(number);
            out.flush();
            System.out.println("request sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAnts() {
        System.out.println("got ants from server");
        try {
            DataInputStream din = new DataInputStream(socket.getInputStream());
            synchronized (model.getAntsVector()) {
                while(din.available() > 0) {
                    if (din.readInt() == 1)
                        loadAntWarrior(din, antsArea);
                    else
                        loadAntWorker(din, antsArea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAnts(DataInputStream din) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            int reciever = din.readInt();
            int sender = din.readInt();
            int number = din.readInt();
            System.out.println("requested: " + reciever + " "  + sender + " " + number);
            dout.writeInt(AntsServer.CODE_ANTS_RESPONSE);
            dout.writeInt(reciever);
            synchronized (model.getAntsVector()) {
                Vector<Ant> antsVector = model.getAntsVector();
                for (int i = 0; i < antsVector.size() && i < number; i++)
                    if (antsVector.get(i) instanceof Warrior)
                        saveAntWarrior(dout, (Warrior)antsVector.get(i));
                    else
                        saveAntWorker(dout, (Worker)antsVector.get(i));
            }
            dout.flush();
            System.out.println("ants sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readClientsUpdate(DataInputStream in) throws IOException {
        synchronized (model.getClientsArray()) {
            model.getClientsArray().clear();
            while (in.available() > 0) {
                model.getClientsArray().add(in.readInt());
            }
            updateClients();
        }
    }

    private void updateClients() {
        simulationController.updateClientsText();
        antRequestController.updateClients();
    }

    private void saveAntWarrior(DataOutputStream out, Warrior ant) throws IOException {
        out.writeInt(1);
        saveAnt(out, ant);
        out.writeInt((int)(ant.getMovementAngle() * 180 / Math.PI));
        out.writeInt(ant.getMovementDirection());
    }

    private void saveAntWorker(DataOutputStream out, Worker ant) throws IOException {
        out.writeInt(0);
        saveAnt(out, ant);
        out.writeInt(ant.getDestination().ordinal());
    }

    private void saveAnt(DataOutputStream out, Ant ant) throws IOException {
        out.writeInt(ant.getPosX());
        out.writeInt(ant.getPosY());
        out.writeInt(ant.getSpawnX());
        out.writeInt(ant.getSpawnY());
        out.writeInt(ant.getLifeTime() - (model.getTime() - ant.getSpawnTime()));  //lifeTime
        out.writeInt(ant.getId());
    }

    private void loadAntWarrior(DataInputStream in, Group antsArea) throws IOException {
        int posX = in.readInt();
        int posY = in.readInt();
        int spawnX = in.readInt();
        int spawnY = in.readInt();
        int lifeTime = in.readInt();
        int id = in.readInt();
        double movementAngle = in.readInt() * Math.PI/180;
        int movementDirection = in.readInt();
        Warrior a = new Warrior(posX, posY, spawnX, spawnY, movementAngle, movementDirection);
        Platform.runLater(
                () -> {
                    a.spawn(antsArea, model.getTime(), lifeTime, id);
                }
        );
        model.getAntsVector().add(a);
        model.getAntsIdsHashSet().add(id);
        model.getAntsSpawnTimeTree().put(id, model.getTime());
        model.setWarriorCount(model.getWarriorCount() + 1);
    }

    private void loadAntWorker(DataInputStream in, Group antsArea) throws IOException {
        int posX = in.readInt();
        int posY = in.readInt();
        int spawnX = in.readInt();
        int spawnY = in.readInt();
        int lifeTime = in.readInt();
        int id = in.readInt();
        int destination = in.readInt();
        Worker a = new Worker(posX, posY, spawnX, spawnY, destination == 0 ? Destination.HOME : Destination.SPAWN);
        Platform.runLater(
                () -> {
                    a.spawn(antsArea, model.getTime(), lifeTime, id);
                }
        );
        model.getAntsVector().add(a);
        model.getAntsIdsHashSet().add(id);
        model.getAntsSpawnTimeTree().put(id, model.getTime());
        model.setWorkerCount(model.getWorkerCount() + 1);
    }

}
