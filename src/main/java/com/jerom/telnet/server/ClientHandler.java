package com.jerom.telnet.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final TelnetServerOwner owner;

    public ClientHandler(Socket socket, TelnetServerOwner owner) {
        this.socket = socket;
        this.owner = owner;
        this.start();
    }

    @Override
    public void run() {
        try {
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            LocalTime curTime = LocalTime.now();
            int compare = 1;
            String logMsg = socket.getInetAddress().getHostName() +
                    " (idle timeout " + Short.valueOf(owner.getSecBeforeDisconnect()) + " sec)";
            writer.println("You connected to server at " + logMsg);
            owner.out("Client connected to server at " + logMsg);
            writer.flush();
            while (!interrupted()) {
                StringBuilder inputLine = new StringBuilder();
                char c = '\n';
                do {
                    while (!reader.ready()&&(compare > 0)) {
                        try {
                            compare = curTime.plusSeconds(owner.getSecBeforeDisconnect()).compareTo(LocalTime.now());
                            sleep(10);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    if (reader.ready()) {
                        curTime = LocalTime.now();
                        c = (char) reader.read();
                        if ((c != '\n') && (c != '\r')) {
                            inputLine.append(c);
                        }
                    }
                    if (compare < 0) {
                        owner.out("Client " + socket.getInetAddress().getHostName() + " disconnected by timeout");
                        writer.println("Timeout " + Short.valueOf(owner.getSecBeforeDisconnect()).toString() + " second to disconnect, goodbye.");
                        writer.flush();
                        socket.close();
                        interrupt();
                    }
                } while ((c != '\n')&&(c != '\r')&&(compare > 0));
                String dreadedLine = inputLine.toString();
                switch (dreadedLine) {
                    case "quit":
                        owner.out("Client " + socket.getInetAddress().getHostName() + " disconnect by query");
                        writer.println("Request to disconnect, goodbye.");
                        writer.flush();
                        socket.close();
                        interrupt();
                        break;
                    case "time":
                        writer.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        writer.flush();
                        break;
                    case "date":
                        writer.println(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                        writer.flush();
                        break;
                }
            }
        } catch (IOException ignored) {
        } finally {
            doBreak();
            owner.removeHandler(this);
        }
    }

    public void doBreak() {
        if (!socket.isClosed()) {
            try {
                // due to lack of time (needs to be implemented correctly)
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
