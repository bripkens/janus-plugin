package de.codecentric.janus.plugin.generation;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

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

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData)
            throws FormException {
        generationConfiguration = req.bindJSON(GenerationConfiguration.class,
                formData);
        save();
        return super.configure(req, formData);
    }

    public FormValidation doCheckCatalogFile(@QueryParameter String catalogFile) {
        return GenerationConfiguration.doCheckCatalogFile(catalogFile);
    }

    public FormValidation doCheckScaffoldDirectory(@QueryParameter String scaffoldDirectory) {
        return GenerationConfiguration
                .doCheckScaffoldDirectory(scaffoldDirectory);
    }

    public FormValidation doCheckTempDirectory(@QueryParameter String tempDirectory) {
        return GenerationConfiguration.doCheckTempDirectory(tempDirectory);
    }
    
    public String getCatalogFile() {
        if (generationConfiguration != null) {
            return generationConfiguration.getCatalogFile();
        }

        return null;
    }

    public String getScaffoldDirectory() {
        if (generationConfiguration != null) {
            return generationConfiguration.getScaffoldDir();
        }

        return null;
    }

    public String getTempDirectory() {
        if (generationConfiguration != null) {
            return generationConfiguration.getTempDir();
        }

        return null;
    }

    public GenerationConfiguration getGenerationConfiguration() {
        return generationConfiguration;
    }
}
