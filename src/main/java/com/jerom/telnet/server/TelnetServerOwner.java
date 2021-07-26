package com.jerom.telnet.server;

public interface TelnetServerOwner {
    void removeHandler(ClientHandler clientHandler);
    void out(String message);
    short getSecBeforeDisconnect();
}
