package de.codecentric.janus.plugin.generation;

import hudson.model.Describable;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class GenerationConfiguration implements Describable<GenerationConfiguration> {
    private String catalogFile, scaffoldDirectory;

    public GenerationConfiguration() {
    }

    @DataBoundConstructor
    public GenerationConfiguration(String catalogFile, String scaffoldDirectory) {
        this.catalogFile = catalogFile;
        this.scaffoldDirectory = scaffoldDirectory;
    }

    public ConfigurationView getDescriptor() {
        return (ConfigurationView) Hudson
                .getInstance()
                .getDescriptor(GenerationConfiguration.class);
    }
}
