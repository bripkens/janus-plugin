package de.codecentric.janus.plugin.bootstrap;

import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class BootstrapLogger {
    private static final Logger LOGGER = Logger
            .getLogger(BootstrapLogger.class.getName());

    private final Log log;
    private final String configurationDump;

    public BootstrapLogger(String configurationDump) {
        this.configurationDump = configurationDump;

        log = new Log();
    }

    public void log(String msg) {
        LOGGER.fine(wrapMessage(msg));
        log.log(msg);
    }

    private String wrapMessage(String msg) {
        return "Janus: " + msg + " [" + configurationDump + "]";
    }

    public void log(String msg, LogEntry.Type type) {
        LOGGER.fine(wrapMessage(msg));
        log.log(msg, type);
    }

    public void setSuccess(boolean success) {
        log.setSuccessful(success);
    }

    public Log getLog() {
        return log;
    }
}
