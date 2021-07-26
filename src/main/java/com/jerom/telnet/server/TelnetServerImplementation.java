package com.jerom.telnet.server;

import com.jerom.telnet.log.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class TelnetServerImplementation extends Thread implements TelnetServer, TelnetServerOwner {

    private Logger logger = null;
    private final short port;
    private final short maxConnections;
    private final short secBeforeDisconnect;
    private ServerSocket serverSocket;
    private volatile boolean isStarted;
    private final HashSet<ClientHandler> clientHandlers = new HashSet<>();

    public TelnetServerImplementation(short port, short maxConnections, short secBeforeDisconnect) {
        isStarted = false;
        this.port = port;
        this.maxConnections = maxConnections;
        this.secBeforeDisconnect = secBeforeDisconnect;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            out(e.getMessage());
        }
    }

    public TelnetServerImplementation addLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    public void out(String message) {
        if (logger != null) {
            logger.out(message);
        }
    }

    @Override
    public short getSecBeforeDisconnect() {
        return secBeforeDisconnect;
    }

    @Override
    public void start() {
        if (!isStarted) {
            isStarted = true;
            super.start();
            out("Telnet server start on " + Short.valueOf(port).toString() + " port (max client connections " +
                        Short.valueOf(maxConnections).toString() + ", idle timeout " +
                        Short.valueOf(maxConnections).toString() + ")");
        }
    }

    @Override
    public boolean isWork() {
        return isStarted;
    }

    @Override
    public void sendStop() {
        if (isStarted) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            interrupt();
        }
    }

    @Override
    public void waitForStopped() {
        while (isWork()) {
            try {
                // due to lack of time (needs to be implemented correctly)
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                try {
                    // wait incoming connection
                    addHandler(serverSocket.accept());
                } catch (IOException ignored) {
                    out("Server has stopped.");
                }
            }
        } finally {
            interruptAllHandlers();
            waitAllHandlers();
            synchronized (serverSocket) {
                try {
                    serverSocket.close();
                } catch (IOException ignored) {
                    // due to lack of time (needs to be implemented correctly)
                }
                isStarted = false;
            }
        }
    }

    private synchronized void addHandler(Socket socket) throws IOException {
        if (clientHandlers.size() < maxConnections) {
            clientHandlers.add(new ClientHandler(socket, this));
        } else {
            socket.close();
        }
    }

    public synchronized void removeHandler(ClientHandler clientHandler) {
        if (clientHandlers.contains(clientHandler)) {
            clientHandlers.remove(clientHandler);
        }
    }

    private synchronized void interruptAllHandlers() {
        for(ClientHandler item : clientHandlers) {
            item.interrupt();
            item.doBreak();
        }
    }

    private synchronized int countHandlers() {
        return clientHandlers.size();
    }

    private void waitAllHandlers() {
        while (countHandlers() > 0) {
            try {
                // due to lack of time (needs to be implemented correctly)
                sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
