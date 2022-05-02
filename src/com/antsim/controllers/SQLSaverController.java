package com.antsim.controllers;

import com.antsim.ai.Destination;
import com.antsim.ant.Ant;
import com.antsim.ant.Warrior;
import com.antsim.ant.Worker;
import com.antsim.model.Habitat;
import javafx.scene.Group;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SQLSaverController {

    Habitat model = Habitat.getInstance();
    String fpath = "src/resources/data/database.properties";
    String url;
    String username;
    String password;
    String tablename;

    public SQLSaverController() {
        try {
            Properties props = new Properties();
            FileReader reader = new FileReader(fpath);
            props.load(reader);
            url = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");
            tablename = props.getProperty("tablename");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void saveAll() {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            statement.executeUpdate("TRUNCATE TABLE " + tablename);
            synchronized (model.getAntsVector()) {
                for (Ant ant : model.getAntsVector()) {
                    if (ant instanceof Warrior)
                        statement.executeUpdate(writeWarrior((Warrior) ant));
                    else
                        statement.executeUpdate(writeWorker((Worker) ant));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWarriors() {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            statement.executeUpdate("TRUNCATE TABLE " + tablename);
            synchronized (model.getAntsVector()) {
                for (Ant ant : model.getAntsVector()) {
                    if (ant instanceof Warrior)
                        statement.executeUpdate(writeWarrior((Warrior) ant));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWorkers() {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            statement.executeUpdate("TRUNCATE TABLE " + tablename);
            synchronized (model.getAntsVector()) {
                for (Ant ant : model.getAntsVector()) {
                    if (ant instanceof Worker)
                        statement.executeUpdate(writeWorker((Worker) ant));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAll(Group root) {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ants");
            synchronized (model.getAntsVector()) {
                while(resultSet.next()) {
                    String type = resultSet.getString(2);
                    Ant ant = type.equals("Warrior") ? readWarrior(resultSet, root) : readWorker(resultSet, root);
                    model.getAntsVector().add(ant);
                    model.getAntsIdsHashSet().add(ant.getId());
                    model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                    if (type.equals("Warrior")) {
                        model.increaseWarriorCount(1);
                    } else {
                        model.increaseWorkerCount(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWarriors(Group root) {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ants WHERE Type='Warrior'");
            synchronized (model.getAntsVector()) {
                while(resultSet.next()) {
                    Ant ant = readWarrior(resultSet, root);
                    model.getAntsVector().add(ant);
                    model.getAntsIdsHashSet().add(ant.getId());
                    model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                    model.increaseWarriorCount(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWorkers(Group root) {
        try (Connection connection =  DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE antsimulation");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ants WHERE Type='Worker'");
            synchronized (model.getAntsVector()) {
                while(resultSet.next()) {
                    Ant ant = readWorker(resultSet, root);
                    model.getAntsVector().add(ant);
                    model.getAntsIdsHashSet().add(ant.getId());
                    model.getAntsSpawnTimeTree().put(ant.getId(), model.getTime());
                    model.increaseWorkerCount(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String writeWarrior(Warrior warrior) {
        String command = "insert into " + tablename + " values (";
        command += warrior.getId() + ", ";
        command += "'Warrior', ";
        command += warrior.getPosX() + ", ";
        command += warrior.getPosY() + ", ";
        command += warrior.getSpawnX() + ", ";
        command += warrior.getSpawnY() + ", ";
        command += warrior.getSpawnTime() + ", ";
        command += warrior.getLifeTime() - (model.getTime() - warrior.getSpawnTime()) + ", ";
        command += (int)(warrior.getMovementAngle() * 180 / Math.PI) + ", ";
        command += warrior.getMovementDirection() + ", NULL)";
        return command;
    }

    private String writeWorker(Worker worker) {
        String command = "insert into " + tablename + " values (";
        command += worker.getId() + ", ";
        command += "'Worker', ";
        command += worker.getPosX() + ", ";
        command += worker.getPosY() + ", ";
        command += worker.getSpawnX() + ", ";
        command += worker.getSpawnY() + ", ";
        command += worker.getSpawnTime() + ", ";
        command += worker.getLifeTime() - (model.getTime() - worker.getSpawnTime()) + ", NULL, NULL, '";
        command += worker.getDestination() + "')";
        return command;
    }

    private Ant readWarrior(ResultSet resultSet, Group antsArea) throws SQLException {
        int id = resultSet.getInt(1);
        int posX = resultSet.getInt(3);
        int posY = resultSet.getInt(4);
        int spawnX = resultSet.getInt(5);
        int spawnY = resultSet.getInt(6);
        int lifetime = resultSet.getInt(8);
        double movementAngle = resultSet.getInt(9) * Math.PI/180;
        int movementDirection = resultSet.getInt(10);
        Warrior warrior = new Warrior(posX, posY, spawnX, spawnY, movementAngle, movementDirection);
        warrior.spawn(antsArea, model.getTime(), lifetime, id);
        return warrior;
    }

    private Ant readWorker(ResultSet resultSet, Group antsArea) throws SQLException {
        int id = resultSet.getInt(1);
        int posX = resultSet.getInt(3);
        int posY = resultSet.getInt(4);
        int spawnX = resultSet.getInt(5);
        int spawnY = resultSet.getInt(6);
        int lifetime = resultSet.getInt(8);
        Destination destination = resultSet.getString(11).equals("Home") ? Destination.HOME : Destination.SPAWN;
        Worker worker = new Worker(posX, posY, spawnX, spawnY, destination);
        worker.spawn(antsArea, model.getTime(), lifetime, id);
        return worker;
    }
}
