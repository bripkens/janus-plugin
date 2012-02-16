package de.codecentric.janus.plugin;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusGenerationException extends RuntimeException{
    public JanusGenerationException() {
    }

    public JanusGenerationException(String message) {
        super(message);
    }

    public JanusGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanusGenerationException(Throwable cause) {
        super(cause);
    }
}
