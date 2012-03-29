package de.codecentric.janus.plugin.bootstrap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Log {
    private boolean successful;
    private final List<LogEntry> entries;

    public Log() {
        entries = new LinkedList<LogEntry>();
    }

    public void log(String msg, LogEntry.Type type) {
        entries.add(new LogEntry(msg, type));
    }

    public void log(String msg) {
        entries.add(new LogEntry(msg, LogEntry.Type.NONE));
    }

    public List<LogEntry> getLogEntries() {
        return Collections.unmodifiableList(entries);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
