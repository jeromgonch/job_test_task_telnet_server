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
            while (!interrupted()) {
                String inputLine = reader.readLine();
                switch (inputLine) {
                    case "quit":
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
        } catch (IOException e) {
            owner.out(e.getMessage());
        } finally {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
            owner.removeHandler(this);
        }
    }
}
