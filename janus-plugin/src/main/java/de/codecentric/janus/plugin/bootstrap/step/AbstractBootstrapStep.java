package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import hudson.model.Build;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBootstrapStep {
    protected final StepExecutionData data;
    protected final GenerationConfiguration generationConfiguration;

    public AbstractBootstrapStep(StepExecutionData data) {
        this.data = data;
        generationConfiguration = GenerationConfiguration.get();
    }

    public abstract boolean execute();

    protected Future<Build> executeJob(String name, Map<String, String> params) {
        data.log("Scheduling build job '" + name + "' with parameters " +
                params);
        return JanusPlugin.scheduleBuild(name, params);
    }
}
