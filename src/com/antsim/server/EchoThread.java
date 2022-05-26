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
                int i = inputStream.read();
                System.out.println(id + " got first input: " + i);
                switch (i) {
                    case -1:
                        lostConnection();
                        break;
                    case AntsServer.CODE_ANTS_REQUEST:
                        int senderId = din.readByte();
                        int number = din.readByte();
                        System.out.println(id + " got request, me: " + id + " sender " + senderId + " number " + number);
                        AntsServer.transferRequest(id, senderId, number);
                        break;
                    case AntsServer.CODE_ANTS_RESPONSE:
                        System.out.println(id + " got response with ants");
                        int reciever = din.read();
                        int sender = din.read();
                        number = din.read();
                        AntsServer.transferResponse(number, din);
                        break;
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
            ByteBuffer bytes = ByteBuffer.allocate(array.size() * 4);
            out.write(AntsServer.CODE_CLIENTS_UPDATE);
            for (Integer i : array)
                bytes.putInt(i);
            out.write(bytes.array());
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }

    public void sendAnts(InputStream in) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.write(AntsServer.CODE_ANTS_RESPONSE);
            while(in.available() > 0)
                dout.write(in.read());
            dout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void requestAnts(int reciever, int sender, int number) {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            System.out.println(id + " Request sent to antsender");
            dout.write(AntsServer.CODE_ANTS_REQUEST);
            dout.write((byte)reciever);
            dout.write((byte)sender);
            dout.write((byte)number);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lostConnection() {
        AntsServer.clientLost(id);
    }


}
