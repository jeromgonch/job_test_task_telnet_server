package com.jerom.telnet;

import com.jerom.telnet.config.TelnetConfig;
import com.jerom.telnet.server.TelnetServer;
import com.jerom.telnet.server.TelnetServerImplementation;

public class Main {
    public static void main(String[] args) {
        // read config parameters
        TelnetConfig telnetConfig = new TelnetConfig(args);
        // make telnet server object
        TelnetServer telnetServer = new TelnetServerImplementation(
                telnetConfig.getPort(),
                telnetConfig.getMaxNumberConnections());
        // start telnet server
        telnetServer.start();
        // waite while server worked
        telnetServer.doWait(200);
    }
}
