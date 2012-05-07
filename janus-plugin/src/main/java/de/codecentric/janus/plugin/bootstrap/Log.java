/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
