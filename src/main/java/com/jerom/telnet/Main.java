package com.jerom.telnet;

import com.jerom.telnet.config.TelnetConfig;

public class Main {
    public static void main(String[] args) {
        // getting configuration
        TelnetConfig telnetConfig = new TelnetConfig(args);
        System.out.println("Start on port " + telnetConfig.getPort());
        System.out.println("Maximum number of incoming connections " + telnetConfig.getMaxNumberConnections());

    }
}
