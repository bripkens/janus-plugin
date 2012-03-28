package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.bootstrap.JanusPluginBootstrapException;
import hudson.model.Build;
import hudson.model.Result;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBuildDependentStep extends AbstractBootstrapStep {

    public AbstractBuildDependentStep(StepExecutionData data) {
        super(data);
    }

    protected abstract String beforeMessage();
    protected abstract String successMessage();
    protected abstract String failMessage(String buildUrl);
    protected abstract String getBuildJobName();
    
    public boolean execute() {
        data.log(beforeMessage());

        String buildJob = getBuildJobName();

        Future<Build> scheduledBuild;
        scheduledBuild = executeJob(buildJob,
                new HashMap<String, String>() {{
                    put("name", data.getProject().getName());
                    put("tmpDir", generationConfig.getTempDir());
                }});


        Build executedBuild;
        try {
            executedBuild = scheduledBuild.get();
        } catch (InterruptedException e) {
            throw new JanusPluginBootstrapException(e);
        } catch (ExecutionException e) {
            throw new JanusPluginBootstrapException(e);
        }

        if (!executedBuild.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
            data.log(failMessage(executedBuild.getUrl()));
            return false;
        }

        data.log(successMessage());
        return true;
    }
}
