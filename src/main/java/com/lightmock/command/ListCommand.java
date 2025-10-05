package com.lightmock.command;

import com.lightmock.core.MockServer;
import picocli.CommandLine.Command;

/**
 * @author Mesut Yakut
 */
@Command(name = "list", description = "List registered routes")
public class ListCommand implements Runnable {
    @Override
    public void run() {
        MockServer server = MockServer.getInstance();
        if (!server.isRunning()) {
            System.out.println("Server is not running.");
            return;
        }
        server.printRoutes();
    }
}
