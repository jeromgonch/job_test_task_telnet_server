package com.jerom.telnet.log;

public class LoggerImplementation implements Logger {

    @Override
    public synchronized void out(String message) {
        System.out.println(message);
    }
}
