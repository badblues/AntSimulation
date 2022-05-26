package com.antsim.controllers;

import com.antsim.ant.Ant;
import com.antsim.ant.Warrior;
import com.antsim.model.Habitat;
import com.antsim.server.AntsServer;
import javafx.scene.Group;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Vector;

public class ClientController extends Thread {

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
                    int i = din.readByte();
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
            out.write(AntsServer.CODE_ANTS_REQUEST);
            out.write((byte) id);
            out.write((byte) number);
            out.flush();
            System.out.println("request sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAnts() {
        System.out.println("got ants from server");
        Habitat model = Habitat.getInstance();
        try {
            ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
            synchronized (model.getAntsVector()) {
                while(oin.available() > 0) {
                    Ant ant = (Ant) oin.readObject();
                    ant.spawn(antsArea, model.getTime(), ant.getLifeTime(), ant.getId());
                    model.getAntsVector().add(ant);
                    model.getAntsIdsHashSet().add(ant.getId());
                    model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                    if (ant instanceof Warrior)
                        model.increaseWarriorCount(1);
                    else
                        model.increaseWorkerCount(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendAnts(DataInputStream din) {
        try {
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
            int reciever = din.readByte();
            int sender = din.readByte();
            int number = din.readByte();
            System.out.println("requested: " + reciever + " "  + sender + " " + number);
            oout.write(AntsServer.CODE_ANTS_RESPONSE);
            oout.write(reciever);
            oout.write(sender);
            oout.write(number);
            synchronized (Habitat.getInstance().getAntsVector()) {
                Vector<Ant> antsVector = Habitat.getInstance().getAntsVector();
                for (int i = 0; i < antsVector.size() && i < number; i++);
                    //oout.writeObject(antsVector.get(i));
            }
            oout.flush();
            System.out.println("ants sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readClientsUpdate(DataInputStream in) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while (in.available() > 0) {
            buffer.put((byte) in.read());
            if (in.available() == 0) {
                synchronized (Habitat.getInstance().getClientsArray()) {
                    Habitat.getInstance().getClientsArray().clear();
                    buffer.rewind();
                    int i;
                    while ((i = buffer.getInt()) != 0) {
                        Habitat.getInstance().getClientsArray().add(i);
                    }
                }
                updateClients();
            }
        }
    }

    private void updateClients() {
        simulationController.updateClientsText();
        antRequestController.updateClients();
    }

}
