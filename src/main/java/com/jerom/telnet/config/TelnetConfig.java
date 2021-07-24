package com.jerom.telnet.config;

public class TelnetConfig {
    final String portParameterName = "TelPort";
    final String portParameterDescription = "Port for incoming connections";
    final String maxNumConnParameterName = "TelSessions";
    final String maxNumConnParameterDescription = "Maximum number of incoming connections";
    private short port = 21; // set default values
    private short maxNumberConnections = 20;

    /**
     * @param commandLineArguments
     * The command-line parameters from main function
     */
    public TelnetConfig(String[] commandLineArguments) {
        System.getenv(portParameterName);
        CmdParamParser parser = new CmdParamParser();
        parser.addParsedParam(portParameterName, "p", portParameterDescription);
        parser.addParsedParam(maxNumConnParameterName, "c", maxNumConnParameterDescription);
        parser.parseCommandLineArguments(commandLineArguments);
        port = parser.getParamValueAsShort(
                portParameterName,
                getEnvironmentVariable(portParameterName, port));
        maxNumberConnections = parser.getParamValueAsShort(
                maxNumConnParameterName,
                getEnvironmentVariable(maxNumConnParameterName, maxNumberConnections));
    }

    public short getPort() {
        return port;
    }

    public short getMaxNumberConnections() {
        return maxNumberConnections;
    }

    private short getEnvironmentVariable(String paramName, short defValue) {
        try {
            return Short.parseShort(System.getenv(paramName));
        } catch (NumberFormatException numberFormatException) {
            return defValue;
        }
    }
}
