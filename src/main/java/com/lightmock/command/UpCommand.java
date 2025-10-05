package com.lightmock.command;

import com.lightmock.core.MockServer;
import picocli.CommandLine;

import java.io.File;

/**
 * @author Mesut Yakut
 */
@CommandLine.Command(name = "up", description = "Start mock server with given config")
public class UpCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "Config file (YAML or JSON)")
    private File configFile;

    @CommandLine.Option(names = {"-p", "--port"}, description = "Port number", defaultValue = "8087")
    private int port;

    @Override
    public void run() {
        try {
            MockServer server = MockServer.getInstance();
            server.start(configFile, port);
            System.out.printf("🚀 LightMock started on port %d with config %s%n", port, configFile.getName());
            Thread.currentThread().join(); // keep alive
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}