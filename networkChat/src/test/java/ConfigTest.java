import config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    private static final Config CONFIG = Config.getInstance();
    protected int port;
    protected String host;

    @BeforeEach
    void setUp() {
        final String path = "src/main/resources/config.properties";
        try (FileReader fileReader = new FileReader(path)) {
            Properties properties = new Properties();
            properties.load(fileReader);

            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetInstance() {
        assertEquals(Config.getInstance(), CONFIG);
    }

    @Test
    void testGetPort() {
        assertEquals(CONFIG.getPort(), port);
    }

    @Test
    void testGetHost() {
        assertEquals(CONFIG.getHost(), host);
    }
}
