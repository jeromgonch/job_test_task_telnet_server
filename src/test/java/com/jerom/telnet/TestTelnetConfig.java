package com.jerom.telnet;

import com.jerom.telnet.config.TelnetConfig;
import org.testng.*;
import org.testng.annotations.*;

public class TestTelnetConfig {

    @Test
    public void testCommandParams1() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter().calculateValues(new String[]{});
            Assert.assertEquals(telnetConfig.getPort(), TelnetConfig.defPort, "default var \"TelPort\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), TelnetConfig.defMaxNumberConnections, "default var \"TelSessions\" value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCommandParams2() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter().calculateValues(new String[]{"-c", "XX", "-p", "XX"});
            Assert.assertEquals(telnetConfig.getPort(), TelnetConfig.defPort, "command line param \"-p\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), TelnetConfig.defMaxNumberConnections, "command line param \"-c\"  value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCommandParams3() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter().calculateValues(new String[]{"-c", "30", "-p", "33"});
            Assert.assertEquals(telnetConfig.getPort(), 33, "command line param \"-p\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), 30, "command line param \"-c\"  value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCommandParams4() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter().calculateValues(new String[]{"--TelPort", "33", "--TelSessions", "30"});
            Assert.assertEquals(telnetConfig.getPort(), 33, "command line param \"TelPort\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), 30, "command line param \"TelSessions\" value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCommandParams5() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter()
                    .addFakeEnvVars("TelPort", "30")
                    .addFakeEnvVars("TelSessions", "40")
                    .calculateValues(new String[]{});
            Assert.assertEquals(telnetConfig.getPort(), 30, "environment var \"TelPort\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), 40, "environment var \"TelSessions\" value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCommandParams6() {
        try {
            TelnetConfig telnetConfig = new TelnetConfigTestAdapter()
                    .addFakeEnvVars("TelPort", "XX")
                    .addFakeEnvVars("TelSessions", "XX")
                    .calculateValues(new String[]{});
            Assert.assertEquals(telnetConfig.getPort(), TelnetConfig.defPort, "environment var \"TelPort\" value failed");
            Assert.assertEquals(telnetConfig.getMaxNumberConnections(), TelnetConfig.defMaxNumberConnections, "environment var \"TelSessions\" value failed");
        } catch (final Exception e) {
            Assert.fail();
        }
    }

}
