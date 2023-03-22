package server;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private int lastId = 0;
    private Queue<Integer> freeId = new LinkedBlockingQueue();
    private Map<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Config.getInstance().getPort())){
            LOGGER.info("server started");

            while (true) {
                Socket socket = serverSocket.accept();
                Integer id = freeId.poll();
                if (id == null) {
                    id = lastId++;
                }
                addConnection(id, new Connection(this, socket, id));
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }
    public void sendToAll(String message) {
        new Thread(() -> {
            for (Connection connection: connections.values()) {
                connection.sendMessage(message);
            }
        }).start();
    }

    public void addConnection(int id, Connection connection) {
        connections.put(id, connection);
        connections.get(id).start();
    }

    public void dropConnection(int id) {
        new Thread(() -> {
            try {
                connections.get(id).close();
                connections.get(id).getSocket().close();
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
            connections.remove(id);
            freeId.offer(id);
        }).start();
    }

    public Queue<Integer> getFreeId() {
        return freeId;
    }

    public Map<Integer, Connection> getConnections() {
        return connections;
    }
}
