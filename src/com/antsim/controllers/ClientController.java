package com.antsim.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientController extends Thread {

    private Socket socket;
    private final SimulationController simulationController;
    public ClientController(SimulationController controller) {
        simulationController = controller;
        try {
            socket = new Socket("localhost", 4999);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String clients = "";
                while((line = reader.readLine()) != null)
                   if (line.isEmpty()) {
                       simulationController.setCurrentClientsText(clients);
                       clients = "";
                   } else {
                       clients += line + "\n";
                   }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
