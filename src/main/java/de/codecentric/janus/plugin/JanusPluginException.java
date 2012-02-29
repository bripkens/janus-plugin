package de.codecentric.janus.plugin;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusPluginException extends RuntimeException{
    public JanusPluginException() {
    }

    public JanusPluginException(String message) {
        super(message);
    }

    public JanusPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanusPluginException(Throwable cause) {
        super(cause);
    }
}
