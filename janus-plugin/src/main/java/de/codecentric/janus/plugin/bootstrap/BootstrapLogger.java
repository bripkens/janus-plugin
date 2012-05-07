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
