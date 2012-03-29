package de.codecentric.janus.plugin.bootstrap;

/**
* @author Ben Ripkens <bripkens.dev@gmail.com>
*/
public class LogEntry {
    public enum Type {
        SUCCESS, FAILURE, NONE
    }

    private final Type type;
    private final String msg;

    public LogEntry(String msg, Type type) {
        this.type = type;
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return type == Type.SUCCESS;
    }

    public boolean isFailure() {
        return type == Type.FAILURE;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                '}';
    }
}
