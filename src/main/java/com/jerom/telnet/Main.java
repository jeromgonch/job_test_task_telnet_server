package com.jerom.telnet;

import com.jerom.telnet.config.TelnetConfig;
import com.jerom.telnet.log.Logger;
import com.jerom.telnet.log.LoggerImplementation;
import com.jerom.telnet.server.TelnetServer;
import com.jerom.telnet.server.TelnetServerImplementation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // initialization logger
        Logger logger = new LoggerImplementation();
        // read config parameters
        TelnetConfig telnetConfig = new TelnetConfig().calculateValues(args);
        // make telnet server object
        TelnetServer telnetServer = new TelnetServerImplementation(
                telnetConfig.getPort(),
                telnetConfig.getMaxNumberConnections(),
                telnetConfig.getSecBeforeDisconnect())
                .addLogger(logger);
        // start telnet server
        telnetServer.start();
        if (telnetServer.isWork()) {
            System.out.println("Press any \"Enter\" for stop server...");
        }
        // wait for quit
        try {
            // wait for user press any key or server will terminate the work
            while ((System.in.available() == 0) && (telnetServer.isWork())) { Thread.sleep(100); }
        } catch (IOException | InterruptedException ignored) {
            // due to lack of time (needs to be implemented correctly)
        }
        // we ask the server to stop
        telnetServer.sendStop();
        // waiting for server stop
        telnetServer.waitForStopped();
    }
}
