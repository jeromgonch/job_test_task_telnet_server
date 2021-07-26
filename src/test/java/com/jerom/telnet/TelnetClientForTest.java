package com.jerom.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TelnetClientForTest {
    private Socket socket;
    private boolean connected;
    private PrintWriter output;
    private BufferedReader input;

    public TelnetClientForTest(short port) {
        try {
            socket = new Socket("localhost", port);
            output = new PrintWriter(socket.getOutputStream());
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            input = new BufferedReader(in);
            connected = true;
        } catch (IOException ignore) {
            connected = false;
        }
    }

    public void close() {
        if (connected) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void write(String text) {
        if (connected) {
            output.write(text + "\r");
            output.flush();
        }
    }

    public String read() {
        if (connected) {
            try {
                if (input.ready()) {
                    return input.readLine(); // !!!!
                } else {
                    return "";
                }
            } catch (IOException ignored) {
                return "error";
            }
        } else {
            return "not connected";
        }
    }

    public boolean getConnected() {
        return connected;
    }
}
