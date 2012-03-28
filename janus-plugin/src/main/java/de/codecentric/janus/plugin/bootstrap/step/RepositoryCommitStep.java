package de.codecentric.janus.plugin.bootstrap.step;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCommitStep extends AbstractBuildDependentStep {

    public RepositoryCommitStep(StepExecutionData data) {
        super(data);
    }

    @Override
    protected String beforeMessage() {
        return "Commiting changes to (remote) repository.";
    }

    @Override
    protected String successMessage() {
        return "Successfully commited all changes.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Failed to commit changes. Build job " + getBuildJobName() +
                "failed. See " + buildUrl + " for details.";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getCommitBuildJob();
    }
}
