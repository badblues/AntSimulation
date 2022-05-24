package com.antsim.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AntsServer {

    static int clientCount = 0;
    static int lastClientId = 0;
    static ArrayList<EchoThread> threadList = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(4999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                assert serverSocket != null : "Not connected";
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            EchoThread thread = new EchoThread(socket, lastClientId++);
            threadList.add(thread);
            thread.start();
            sendClientsUpdate();
        }
    }

    public static void clientLost(int id) {
        threadList.removeIf(ob -> (ob.getClientId() == id));
        sendClientsUpdate();
    }

    public static String getClientNames() {
        StringBuilder names = new StringBuilder();
        for (EchoThread thread : threadList) {
            names.append("Client ").append(thread.getClientId()).append("\n");
        }
        return names.toString();
    }

    private static void sendClientsUpdate() {
        for (EchoThread thread : threadList) {
            thread.updateClientsList();
        }
    }
}
