package com.jerom.telnet.server;

import java.io.IOException;
import java.net.ServerSocket;

public class TelnetServerImplementation implements TelnetServer {

    private short port;
    private short maxConnections;
    private volatile ServerSocket serverSocket = null;
    private String status;

    public TelnetServerImplementation(short port, short maxConnections) {
        this.port = port;
        this.maxConnections = maxConnections;
        status = "";
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            status = "Telnet server start on " + Short.valueOf(port).toString() + " port (max client connections " +
                    Short.valueOf(maxConnections).toString() + ")";
        } catch (IOException e) {
            status = e.getMessage();
        }
    }

    @Override
    public boolean isWork() {
        return serverSocket != null;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void sendStop() {

    }

    @Override
    public void doWait(long millis) {
        while (isWork()) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                sendStop();
            }
        }
    }
}
