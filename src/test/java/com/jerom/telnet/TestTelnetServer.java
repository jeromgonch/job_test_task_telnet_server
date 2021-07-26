package com.jerom.telnet;

import com.jerom.telnet.server.TelnetServer;
import com.jerom.telnet.server.TelnetServerImplementation;
import org.testng.*;
import org.testng.annotations.*;

import java.util.regex.Pattern;

public class TestTelnetServer {

    @Test
    public void testTelnetServer1() {
        try {
            TelnetServer telnetServer = new TelnetServerImplementation((short) 21, (short) 3, (short) 600);
            try {
                telnetServer.start();
                if (telnetServer.isWork()) {
                    TelnetClientForTest client = new TelnetClientForTest((short) 21);
                    sleep(500);
                    try {
                        if (!client.getConnected()) {
                            Assert.fail("Client not connected to server!");
                        } else {
                            String text = client.read();
                            Pattern pattern = Pattern.compile("You connected to server at .* \\(idle timeout 600 sec\\)");
                            Assert.assertTrue(pattern.matcher(text).matches());

                            client.write("date");
                            sleep(500);
                            text = client.read();
                            pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}");
                            Assert.assertTrue(pattern.matcher(text).matches());

                            client.write("time");
                            sleep(500);
                            text = client.read();
                            pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
                            Assert.assertTrue(pattern.matcher(text).matches());

                            client.write("quit");
                            sleep(500);
                            text = client.read();
                            Assert.assertEquals("Request to disconnect, goodbye.", text);
                        }
                    } finally {
                        client.close();
                    }
                } else {
                    Assert.fail("Server not started!");
                }
            } finally {
                if (telnetServer.isWork()) {
                    telnetServer.sendStop();
                    telnetServer.waitForStopped();
                }
            }
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testTelnetServer2() {
        try {
            TelnetServer telnetServer = new TelnetServerImplementation((short) 21, (short) 3, (short) 2);
            try {
                telnetServer.start();
                if (telnetServer.isWork()) {
                    TelnetClientForTest client = new TelnetClientForTest((short) 21);
                    sleep(500);
                    try {
                        if (!client.getConnected()) {
                            Assert.fail("Client not connected to server!");
                        } else {
                            String text = client.read();
                            Pattern pattern = Pattern.compile("You connected to server at .* \\(idle timeout 2 sec\\)");
                            Assert.assertTrue(pattern.matcher(text).matches());

                            sleep(2500);
                            text = client.read();
                            Assert.assertEquals("Timeout 2 second to disconnect, goodbye.", text);
                        }
                    } finally {
                        client.close();
                    }
                } else {
                    Assert.fail("Server not started!");
                }
            } finally {
                if (telnetServer.isWork()) {
                    telnetServer.sendStop();
                    telnetServer.waitForStopped();
                }
            }
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
