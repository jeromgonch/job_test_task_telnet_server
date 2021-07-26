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
     * send a stop signal to the server
     */
    void sendStop();


    /**
     * waiting for the server to stop after
     */
    void waitForStopped();

}
