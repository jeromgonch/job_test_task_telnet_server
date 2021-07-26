package com.jerom.telnet.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoggerImplementation implements Logger {
    private File file;

    public LoggerImplementation() {
        try {
            Path path = Paths.get(this.getClass().getResource(".").toURI())
                    .getParent().getParent().getParent().getParent().getParent();
            file = new File(path.toFile(), "log.txt");
        } catch (URISyntaxException e) {
            file = null;
        }
    }

    @Override
    public synchronized void out(String message) {
        // System.out.println(message);
        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file.getPath(), true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss: ")) + message + "\r");
                bufferWriter.close();
            } catch (IOException ignore) {
            }
        }
    }
}
