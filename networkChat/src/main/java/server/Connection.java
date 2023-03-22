package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Connection extends Thread {
    private Server server;
    private Socket socket;
    private int id;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm:ss");
    private String name;
    private final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private BufferedReader in;
    private PrintWriter out;

    public Connection(Server server, Socket socket, int id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

    }

    @Override
    public void run() {
        try {
            StringBuilder message = new StringBuilder();

            sendMessage("Введите имя");

            while (true) {
                String text = in.readLine();

                message.append(dtf.format(LocalDateTime.now()));
                message.append(" ");

                if (text.equals(Commands.EXIT.getCommand())) {
                    text = "покидает чат";
                }

                if (name == null) {
                    name = text;
                    text = "присоединяется к чату";
                }

                message.append(name);
                message.append(": ");
                message.append(text);

                server.sendToAll(message.toString());
                LOGGER.info(message.toString());

                message.delete(0, message.length() - 1);

                if (text.equals("покидает чат")) {
                    server.dropConnection(id);
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
            server.dropConnection(id);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
