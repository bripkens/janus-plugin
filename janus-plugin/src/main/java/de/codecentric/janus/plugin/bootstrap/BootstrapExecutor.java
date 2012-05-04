package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.bootstrap.step.*;
import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.CatalogEntry;
import hudson.model.Build;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BootstrapExecutor {

    private final BootstrapLogger logger;
    private final ParsedFormData data;
    private final AtomicBoolean atomicBoolean;

    private final AbstractBootstrapStep[] steps;

    BootstrapExecutor(ParsedFormData data) {
        this.data = data;
        this.logger = new BootstrapLogger(data.toString());
        atomicBoolean = new AtomicBoolean();

        steps = new AbstractBootstrapStep[] {
                new RepositoryCreationStep(data, logger),
                new RepositoryCheckoutStep(data, logger),
                new SourceCodeGenerationStep(data, logger),
                new RepositoryCommitStep(data, logger),
                new JenkinsJobCreationStep(data, logger),
                new JiraConfigurationStep(data, logger)
        };
    }

    Log execute() {
        if (!atomicBoolean.compareAndSet(false, true)) {
            throw new IllegalStateException("A bootstrap executor may only " +
                    "be used once. Please create a new instance.");
        }

        logger.log("Initiating project bootstrap for: " +
                data.getProject().getName() + ".");

        boolean allSuccessful = true;
        try {
            for (int i = 0; i < steps.length && allSuccessful; i++) {
                allSuccessful = steps[i].execute();
            }
        } catch (JanusPluginBootstrapException ex) {
            String msg = "Unexpected project bootstrap failure: " +
                    ex.getMessage();
            logger.log(msg, LogEntry.Type.FAILURE);
            allSuccessful = false;
        }

        if (allSuccessful) {
            logger.log("Hooray, successfully finished project bootstrap!",
                    LogEntry.Type.SUCCESS);
            logger.setSuccess(true);
        } else {
            logger.log("Stopping bootstrap because of previous errors.",
                    LogEntry.Type.FAILURE);
        }

        return logger.getLog();
    }
}
