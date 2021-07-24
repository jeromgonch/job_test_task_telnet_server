package com.jerom.telnet.config;

import org.apache.commons.cli.*;

public class CmdParamParser {
    private Options options = new Options();
    private CommandLineParser parser = new DefaultParser();
    private HelpFormatter formatter = new HelpFormatter();
    private CommandLine cmd;

    public void addParsedParam(String parameterName, String parameterShortName, String description) {
        Option item = new Option(parameterShortName, parameterName, true, description);
        item.setRequired(false);
        options.addOption(item);
    }

    public void parseCommandLineArguments(String[] commandLineArguments) {
        try {
            cmd = parser.parse(options, commandLineArguments);
        } catch (ParseException parseException) {
            System.out.println(parseException.getMessage());
            formatter.printHelp("Description: ", options);
            System.exit(1);
        }
    }

    public short getParamValueAsShort(String parameterName, short defaultValue) {
        try {
            return Short.parseShort(cmd.getOptionValue(parameterName, Short.valueOf(defaultValue).toString()));
        } catch (NumberFormatException numberFormatException) {
            return defaultValue;
        }
    }

    public String getParamValueAsString(String parameterName, String defaultValue) {
        return cmd.getOptionValue(parameterName, defaultValue);
    }
}
