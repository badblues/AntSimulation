package com.antsim.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
                if (socket.getInputStream().read() == -1)
                    lostConnection();
            } catch (IOException e) {
                lostConnection();
                break;
            }
        }
    }

    public void updateClientsList() {
        StringBuilder message = new StringBuilder();
        message.append("Current client: client ").append(id).append("\n----------\n");
        message.append(AntsServer.getClientNames());
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(message);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }

    private void lostConnection() {
        AntsServer.clientLost(id);
    }
}
