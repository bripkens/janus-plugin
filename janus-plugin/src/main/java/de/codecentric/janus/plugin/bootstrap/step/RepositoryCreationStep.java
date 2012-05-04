package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.JanusPluginBootstrapException;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import hudson.model.Build;
import hudson.model.Result;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCreationStep extends AbstractBuildDependentStep {

    public RepositoryCreationStep(ParsedFormData data,
                                  BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    protected String beforeMessage() {
        return "Creating repository.";
    }

    @Override
    protected String successMessage() {
        return "Repository creation successful.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Repository creation build job failed. Please see build" +
                "job '" + getBuildJobName() + "' for more information (" +
                buildUrl + ").";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getGenerationBuildJob();
    }
}
