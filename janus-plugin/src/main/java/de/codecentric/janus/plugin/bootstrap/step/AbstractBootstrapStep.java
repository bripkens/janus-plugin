package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.scaffold.Scaffold;
import hudson.model.Build;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBootstrapStep {
    protected final ParsedFormData data;
    protected final BootstrapLogger logger;
    protected final GenerationConfiguration generationConfig;

    public AbstractBootstrapStep(ParsedFormData data, BootstrapLogger logger) {
        this.data = data;
        this.logger = logger;
        generationConfig = GenerationConfiguration.get();
    }

    public abstract boolean execute();

    protected Future<Build> executeJob(String name, Map<String, String> params) {
        logger.log("Scheduling build job '" + name + "' with parameters " +
                params);
        return JanusPlugin.scheduleBuild(name, params);
    }

    protected Scaffold getScaffold() {
        String scaffoldDir;
        scaffoldDir = ensureFileSeparator(generationConfig.getScaffoldDir());
        String scaffoldFileName = data.getScaffold().getFilename();
        return Scaffold.from(new File(scaffoldDir + scaffoldFileName));
    }

    protected File getOutputDir() {
        String tmpDir = ensureFileSeparator(generationConfig.getTempDir());
        String outputDir = tmpDir + data.getProject().getName();
        return new File(outputDir);
    }

    protected String ensureFileSeparator(String path) {
        if (path.endsWith(File.separator)) {
            return path;
        } else {
            return path + File.separator;
        }
    }
}
