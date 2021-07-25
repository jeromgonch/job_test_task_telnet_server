package com.jerom.telnet.config;

public class TelnetConfig {
    public final static short defPort = 21;
    public final static short defMaxNumberConnections = 20;
    final String portParameterName = "TelPort";
    final String portParameterNameShort = "p";
    final String portParameterDescription = "Port for incoming connections";
    final String maxNumConnParameterName = "TelSessions";
    final String maxNumConnParameterNameShort = "c";
    final String maxNumConnParameterDescription = "Maximum number of incoming connections";
    private short port = defPort; // set default values
    private short maxNumberConnections = defMaxNumberConnections;

    /**
     * @param commandLineArguments
     * The command-line parameters from main function
     */
    public TelnetConfig calculateValues(String[] commandLineArguments) {
        CmdParamParser parser = new CmdParamParser();
        parser.addParsedParam(portParameterName, portParameterNameShort, portParameterDescription);
        parser.addParsedParam(maxNumConnParameterName, maxNumConnParameterNameShort, maxNumConnParameterDescription);
        parser.parseCommandLineArguments(commandLineArguments);
        port = parser.getParamValueAsShort(
                portParameterName,
                getEnvironmentVariable(portParameterName, port));
        maxNumberConnections = parser.getParamValueAsShort(
                maxNumConnParameterName,
                getEnvironmentVariable(maxNumConnParameterName, maxNumberConnections));
        return this;
    }

    public short getPort() { return port; }

    public short getMaxNumberConnections() {
        return maxNumberConnections;
    }

    private short getEnvironmentVariable(String paramName, short defValue) {
        try {
            return Short.parseShort(getenv(paramName));
        } catch (NumberFormatException numberFormatException) {
            return defValue;
        }
    }

    // made separate method for implementing unit tests
    protected String getenv(String name) {
        return System.getenv(name);
    }
}
