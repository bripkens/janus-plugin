package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.bootstrap.Log;
import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.CatalogEntry;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StepExecutionData {
    private static final Logger LOGGER = Logger
            .getLogger(StepExecutionData.class.getName());

    private final Project project;
    private final VCSConfiguration vcsConfiguration;
    private final CIConfiguration ciConfiguration;
    private final CatalogEntry catalogEntry;
    private final Map<String, String> context;

    private final Log log;

    public StepExecutionData(Project project, VCSConfiguration vcsConfiguration,
                             CIConfiguration ciConfiguration,
                             CatalogEntry catalogEntry,
                             Map<String, String> context) {
        this.project = project;
        this.vcsConfiguration = vcsConfiguration;
        this.ciConfiguration = ciConfiguration;
        this.catalogEntry = catalogEntry;
        this.context = Collections.unmodifiableMap(context);

        log = new Log();
    }

    public Log getLog() {
        return log;
    }

    public Project getProject() {
        return project;
    }

    public VCSConfiguration getVcsConfiguration() {
        return vcsConfiguration;
    }

    public CIConfiguration getCiConfiguration() {
        return ciConfiguration;
    }

    public CatalogEntry getCatalogEntry() {
        return catalogEntry;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void log(String msg) {
        String wrappedMsg = "Janus: " + msg + " [" + this.toString() + "]";
        LOGGER.info(wrappedMsg);
        log.log(msg);
    }

    public void log(String msg, LogEntry.Type type) {
        String wrappedMsg = "Janus: " + msg + " [" + this.toString() + "]";
        LOGGER.info(wrappedMsg);
        log.log(msg, type);
    }

    public void setSuccess(boolean success) {
        log.setSuccessful(success);
    }

    @Override
    public String toString() {
        return "StepExecutionData{" +
                "project=" + project +
                ", vcsConfiguration=" + vcsConfiguration +
                ", ciConfiguration=" + ciConfiguration +
                ", catalogEntry=" + catalogEntry +
                ", context=" + context +
                '}';
    }
}
