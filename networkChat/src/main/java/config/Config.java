package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.util.Properties;

public class Config {
    private static Config instance;
    private final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private int port;
    private String host;
    private final int DEFAULT_PORT = 9010;
    private final String DEFAULT_HOST = "127.0.0.1";
    private static final String PATH = "src/main/resources/config.properties";

    private Config() {
        try (FileReader fileReader = new FileReader(PATH)) {
            Properties properties = new Properties();
            properties.load(fileReader);
            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("host");
        } catch (Exception e) {
            LOGGER.error(e.toString());
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
