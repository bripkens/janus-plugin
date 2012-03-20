package de.codecentric.janus.plugin.generation;

import de.codecentric.janus.VersionControlSystem;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.QueryParameter;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class ConfigurationView extends Descriptor<GenerationConfiguration> {

    private GenerationConfiguration generationConfiguration;

    public ConfigurationView() {
        super(GenerationConfiguration.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Janus Generation Configuration";
    }

    public FormValidation doCheckCatalogFile(@QueryParameter String catalogFile) {
        if (catalogFile.trim().isEmpty()) {
            return FormValidation.warning("Please enter a catalog file location.");
        }

        return FormValidation.ok();
    }

    public FormValidation doCheckScaffoldDirectory(@QueryParameter String scaffoldDirectory) {
        if (scaffoldDirectory.trim().isEmpty()) {
            return FormValidation.warning("Please enter a scaffoldDirectory location.");
        }

        return FormValidation.ok();
    }
}
