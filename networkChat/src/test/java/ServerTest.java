import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.Connection;
import server.Server;

import java.net.Socket;

public class ServerTest {
    private final Server server = new Server();
    private Connection connection;
    private Socket socket;

    @BeforeEach
    void setUp() {
        connection = Mockito.mock(Connection.class);
        socket = Mockito.mock(Socket.class);
        Mockito.when(connection.getSocket()).thenReturn(socket);
        server.addConnection(1, connection);
        server.addConnection(2, connection);
    }

    @Test
    void testDropConnection() {
        int connectionIdToRemove = 1;
        int sizeBeforeDrop = server.getConnections().size();
        server.dropConnection(connectionIdToRemove);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(server.getConnections().size() == sizeBeforeDrop - 1);
        Assertions.assertTrue(server.getConnections().get(connectionIdToRemove) == null);
        Assertions.assertTrue(server.getFreeId().peek() == connectionIdToRemove);
    }

    @Test
    void testAddConnection() {
        int sizeBeforeAdd = server.getConnections().size();
        server.addConnection(3, connection);
        Assertions.assertTrue(server.getConnections().size() == sizeBeforeAdd + 1);
    }

    @Test
    void testSendToAll() {
        server.sendToAll("message");
        Mockito.verify(connection, Mockito.times(2)).sendMessage("message");
    }
}
