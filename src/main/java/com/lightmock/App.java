package com.lightmock;


import com.lightmock.command.ListCommand;
import com.lightmock.command.UpCommand;
import picocli.CommandLine;

/**
 * @author Mesut Yakut
 */
@CommandLine.Command(
        name = "lightmock",
        aliases = {"lmock"},
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        subcommands = {UpCommand.class, ListCommand.class}
)
public class App implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("LightMock - Lightweight Mock Server CLI");
        System.out.println("Usage: lightmock up <config.yaml|json> --port 8087");
        System.out.println("Press Ctrl+C top stop the server.");
    }
}
