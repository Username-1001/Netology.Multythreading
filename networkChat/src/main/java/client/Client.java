package client;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Commands;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private AtomicBoolean exit = new AtomicBoolean(false);
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client() {
        try {
            socket = new Socket(Config.getInstance().getHost(), Config.getInstance().getPort());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error(e.toString());
            exit.set(true);
        }
    }

    public void start() {
        receiveMessages();
        sendMessages();
    }

    public void sendMessages() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!exit.get()) {
                String message = scanner.nextLine();
                out.println(message);
                LOGGER.info("Отправка сообщения: " + message);
                if (message.equals(Commands.EXIT.getCommand())) {
                    exit.set(true);
                    LOGGER.info("Завершение работы клиента");
                    close();
                }
            }
            scanner.close();
        }).start();
    }

    public void receiveMessages() {
        new Thread(() -> {
            try {
                while (!exit.get()) {
                    String message = in.readLine();
                    System.out.println(message);
                    LOGGER.info(message);
                }
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        }).start();
    }

    private void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }
}
