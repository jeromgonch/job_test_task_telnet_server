package com.jerom.telnet;

import com.jerom.telnet.config.TelnetConfig;

import java.util.HashMap;
import java.util.Map;

/**
 *TelnetConfig class unit test adapter
 */
public class TelnetConfigTestAdapter extends TelnetConfig {

    private Map<String, String> fakeEnvVars = new HashMap<>();

    /**
     * @param varName
     * name of environment variable
     * @param varValue
     * value of environment variable
     * @return
     * "this" for Fluent Builder style use
     */
    public TelnetConfigTestAdapter addFakeEnvVars(String varName, String varValue) {
        fakeEnvVars.put(varName, varValue);
        return this;
    }

    @Override
    protected String getenv(String name) {
        if (fakeEnvVars.containsKey(name)) {
            return fakeEnvVars.get(name);
        } else {
            return null;
        }
    }

}
