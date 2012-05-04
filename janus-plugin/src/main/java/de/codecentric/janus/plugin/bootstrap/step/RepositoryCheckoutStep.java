package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCheckoutStep extends AbstractBuildDependentStep {

    public RepositoryCheckoutStep(ParsedFormData data,
                                  BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    protected String beforeMessage() {
        return "Checking out repository";
    }

    @Override
    protected String successMessage() {
        return "Successfully checked out repository into temporary directory.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Failed to check out the repository. Please see build " +
                getBuildJobName() + " for details (" + buildUrl + ").";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getCheckoutBuildJob();
    }
}
