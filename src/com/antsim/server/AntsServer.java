package com.antsim.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AntsServer {

    static int lastClientId = 1;
    static ArrayList<EchoThread> threadList = new ArrayList<>();
    static public final int CODE_CLIENTS_UPDATE = 0;
    static public final int CODE_ANTS_REQUEST = 1;
    static public final int CODE_ANTS_RESPONSE = 2;
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

    public static ArrayList<Integer> getClients() {
        ArrayList<Integer> array = new ArrayList<>();
        for (EchoThread thread : threadList)
            array.add(thread.getClientId());
        return array;
    }

    private static void sendClientsUpdate() {
        for (EchoThread thread : threadList) {
            thread.updateClientsList();
        }
    }

    public static void transferRequest(int recieverId, int senderId, int number) {
        System.out.println("transfer request");
        for (EchoThread thread : threadList)
            if (thread.getClientId() == senderId) {
                thread.requestAnts(recieverId, senderId, number);
            }
    }

    public static void transferResponse(int recieverId, ArrayList<Integer> array) {
        System.out.println("transfer response");
        for (EchoThread thread : threadList)
            if (thread.getClientId() == recieverId)
                thread.sendAnts(array);
    }
}
