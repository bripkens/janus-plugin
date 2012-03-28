package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.plugin.JanusPluginException;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusPluginBootstrapException extends JanusPluginException {
    public JanusPluginBootstrapException() {
    }

    public JanusPluginBootstrapException(String message) {
        super(message);
    }

    public JanusPluginBootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanusPluginBootstrapException(Throwable cause) {
        super(cause);
    }
}
