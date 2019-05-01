package server;

import exception.ConfigurationException;
import handler.RequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cosminoprea on 5/1/19.
 */
public class WebServer implements Runnable {

    private static Logger LOGGER = Logger.getLogger(WebServer.class.getName());
    private ServerSocket server;
    private static Integer serverPort = 8080;
    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue connections;
    private static Integer poolSize = 5;

    public void run() {
        try {
            server = new ServerSocket(serverPort);
            connections = new LinkedBlockingQueue<>();
            threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 10, TimeUnit.SECONDS, connections);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                threadPoolExecutor.execute(new Thread(new RequestHandler(server.accept())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        try {
            loadProperties();
        } catch (NumberFormatException e) {
            throw new ExceptionInInitializerError("Bad configuration in application.properties.");
        }
    }

    private static void loadProperties() throws NumberFormatException {
        Properties prop = new Properties();
        String propFileName = "application.properties";

        InputStream inputStream = WebServer.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                LOGGER.info("Default properties will be loaded.");
            }
        } else {
            LOGGER.info("Default properties will be loaded.");
        }
        if (prop.getProperty("server.port")!=null && !prop.getProperty("server.port").isEmpty()) {
            serverPort = Integer.parseInt(prop.getProperty("server.port"));

        }
        if (prop.getProperty("server.pool-size")!=null && !prop.getProperty("server.pool-size").isEmpty()) {
            poolSize = Integer.parseInt(prop.getProperty("server.pool-size"));

        }
    }
}
