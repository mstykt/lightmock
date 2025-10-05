package com.lightmock.core;

import com.lightmock.model.Endpoint;
import com.lightmock.util.ConfigParser;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

/**
 * @author Mesut Yakut
 */
public class MockServer {

    private static final MockServer INSTANCE = new MockServer();
    private HttpServer server;
    private List<Endpoint> endpoints = Collections.emptyList();
    private int port;
    private volatile boolean running = false;
    private static final int DEFAULT_PORT = 8087;

    private MockServer() {}

    public static MockServer getInstance() {
        return INSTANCE;
    }

    public synchronized void start(File configFile) throws IOException {
        start(configFile, DEFAULT_PORT);
    }

    public synchronized void start(File configFile, int port) throws IOException {
        if (running) throw new IllegalStateException("Server already running");

        this.endpoints = ConfigParser.load(configFile);
        this.port = port;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler(endpoints));
        server.start();

        running = true;
        printRoutes();
    }

    public synchronized void stop() {
        if (!running) return;
        server.stop(0);
        running = false;
        System.out.println("LightMock stopped");
    }

    public boolean isRunning() {
        return running;
    }

    public void printRoutes() {
        System.out.println("Active endpoints:");
        if (endpoints != null) {
            for (Endpoint ep : endpoints) {
                Endpoint.Request req = ep.getRequest();
                if (req != null) {
                    System.out.printf("%s %s  (strictBody=%s)%n",
                            req.getMethod(),
                            req.getPath(),
                            ep.isStrictBody());
                } else {
                    System.out.println("Invalid endpoint: request is null");
                }
            }
        }
        System.out.println("=======================");
    }
}