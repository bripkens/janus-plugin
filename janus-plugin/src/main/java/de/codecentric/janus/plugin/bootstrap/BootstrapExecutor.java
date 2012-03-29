package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.bootstrap.step.*;
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

    private static final Logger LOGGER = Logger
            .getLogger(BootstrapExecutor.class.getName());

    private final StepExecutionData data;
    private final AtomicBoolean atomicBoolean;

    private final AbstractBootstrapStep[] steps;

    BootstrapExecutor(Project project, VCSConfiguration vcsConfiguration,
                      CatalogEntry catalogEntry, Map<String, String> context) {

        data = new StepExecutionData(project,
                vcsConfiguration,
                catalogEntry,
                context);

        atomicBoolean = new AtomicBoolean();

        steps = new AbstractBootstrapStep[] {
                new RepositoryCreationStep(data),
                new RepositoryCheckoutStep(data),
                new SourceCodeGenerationStep(data),
                new RepositoryCommitStep(data)
        };
    }

    Log execute() {
        if (!atomicBoolean.compareAndSet(false, true)) {
            throw new IllegalStateException("A bootstrap executor may only " +
                    "be used once. Please create a new instance.");
        }

        data.log("Initiating project bootstrap for: " +
                data.getProject().getName() + ".");

        boolean allSuccessful = true;
        try {
            for (int i = 0; i < steps.length && allSuccessful; i++) {
                allSuccessful = steps[i].execute();
            }
        } catch (JanusPluginBootstrapException ex) {
            String msg = "Unexpected project bootstrap failure: " +
                    ex.getMessage();
            data.log(msg, LogEntry.Type.FAILURE);
            LOGGER.log(Level.WARNING, msg, ex);
            allSuccessful = false;
        }

        if (allSuccessful) {
            data.log("Hooray, successfully finished project bootstrap!",
                    LogEntry.Type.SUCCESS);
            data.setSuccess(true);
        } else {
            data.log("Stopping bootstrap because of previous errors.",
                    LogEntry.Type.FAILURE);
        }

        return data.getLog();
    }
}
