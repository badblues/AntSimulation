package com.antsim.server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

//TODO create new dialog window with "request ant" feature
//TODO create "request ant" feature

public class EchoThread extends Thread {
    private Socket socket;
    private final int id;

    public EchoThread(Socket clientSocket, int id) {
        socket = clientSocket;
        this.id = id;
    }

    public void run() {
        while(true) {
            try {
                InputStream inputStream = socket.getInputStream();
                DataInputStream din = new DataInputStream(inputStream);
                int i = din.readInt();
                switch (i) {
                    case -1 -> lostConnection();
                    case AntsServer.CODE_ANTS_REQUEST -> {
                        int senderId = din.readInt();
                        int number = din.readInt();
                        System.out.println(id + " got request: " + id + " sender " + senderId + " number " + number);
                        AntsServer.transferRequest(id, senderId, number);
                    }
                    case AntsServer.CODE_ANTS_RESPONSE -> {
                        int reciever = din.readInt();
                        System.out.println(id + " got response with ants: " + reciever);
                        ArrayList<Integer> array = new ArrayList<>();
                        while(din.available() > 0)
                            array.add(din.readInt());
                        AntsServer.transferResponse(reciever, array);
                    }
                }
            } catch (IOException e) {
                lostConnection();
                break;
            }
        }
    }

    public void updateClientsList() {
        ArrayList<Integer> array = AntsServer.getClients();
        array.add(0, id);
        try {
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            out.writeInt(AntsServer.CODE_CLIENTS_UPDATE);
            for (Integer i : array)
                out.writeInt(i);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }

    public void sendAnts(ArrayList<Integer> array) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeInt(AntsServer.CODE_ANTS_RESPONSE);
            for (Integer i : array)
                dout.writeInt(i);
            dout.flush();
            System.out.println("sent ants to reciever: " + id);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void requestAnts(int reciever, int sender, int number) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            System.out.println(id + " Request sent to antsender:" + reciever + " " + sender + " " + number);
            dout.writeInt(AntsServer.CODE_ANTS_REQUEST);
            dout.writeInt(reciever);
            dout.writeInt(sender);
            dout.writeInt(number);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lostConnection() {
        AntsServer.clientLost(id);
    }


}
