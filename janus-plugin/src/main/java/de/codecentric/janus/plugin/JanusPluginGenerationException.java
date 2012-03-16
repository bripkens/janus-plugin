package de.codecentric.janus.plugin;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusPluginGenerationException extends JanusPluginException {
    public JanusPluginGenerationException() {
    }

    public JanusPluginGenerationException(String message) {
        super(message);
    }

    public JanusPluginGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanusPluginGenerationException(Throwable cause) {
        super(cause);
    }
}
