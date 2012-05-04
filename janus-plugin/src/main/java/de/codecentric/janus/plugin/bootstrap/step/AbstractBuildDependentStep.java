package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.JanusPluginBootstrapException;
import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import hudson.model.Build;
import hudson.model.Result;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBuildDependentStep extends AbstractBootstrapStep {

    public AbstractBuildDependentStep(ParsedFormData data,
                                      BootstrapLogger logger) {
        super(data, logger);
    }

    protected abstract String beforeMessage();
    protected abstract String successMessage();
    protected abstract String failMessage(String buildUrl);
    protected abstract String getBuildJobName();
    
    public boolean execute() {
        logger.log(beforeMessage());

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
            logger.log(failMessage(executedBuild.getUrl()),
                    LogEntry.Type.FAILURE);
            return false;
        }

        logger.log(successMessage(), LogEntry.Type.SUCCESS);
        return true;
    }
}
