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
    private ServerSocket serverSocket = null;
    private final HashSet<ClientHandler> clientHandlers = new HashSet<>();

    public TelnetServerImplementation(short port, short maxConnections) {
        this.port = port;
        this.maxConnections = maxConnections;
    }

    public TelnetServerImplementation addLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public void out(String message) {
        if (logger != null) {
            logger.out(message);
        }
    }

    @Override
    public void start() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
                super.start();
                out("Telnet server start on " + Short.valueOf(port).toString() + " port (max client connections " +
                        Short.valueOf(maxConnections).toString() + ")");
            } catch (IOException e) {
                out(e.getMessage());
            }
        }
    }

    @Override
    public boolean isWork() {
        synchronized (serverSocket) {
            return serverSocket != null;
        }
    }

    @Override
    public void sendStop() {
        interrupt();
    }

    @Override
    public void doWait(long millis) {
        while (isWork()) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
            }
        }
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                try {
                    // wait incoming connection
                    addHandler(serverSocket.accept());
                } catch (IOException e) {
                    out(e.getMessage());
                }
            }
        } finally {
            synchronized (serverSocket) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    out(e.getMessage());
                }
                serverSocket = null;
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

}
