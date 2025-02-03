package org.example;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "App", version = "App 1.0-SNAPSHOT", mixinStandardHelpOptions = true)
@Slf4j
public class App implements Runnable {


    @Option(names = { "-p", "--port" }, description = "port to listen on") 
    int port = 8080;

    @Override
    public void run() {
        log.info(String.format("Hello, my port is %d!", port));
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new App()).execute(args));
    }

}
