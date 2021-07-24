package com.jerom.telnet.server;

public interface TelnetServer {

    /**
     * start telnet server
     */
    void start();

    /**
     * checks for "server is work"
     * @return true - server is work
     */
    boolean isWork();

    /**
     * @return server status string
     */
    String getStatus();

    /**
     * send a stop signal to the server
     */
    void sendStop();

    /**
     * Waiting for the server to stop
     * @param millis sets the frequency of checks for "server is work"
     */
    void doWait(long millis);
}
