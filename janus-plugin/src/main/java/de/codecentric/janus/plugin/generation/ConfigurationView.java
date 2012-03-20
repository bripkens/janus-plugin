package de.codecentric.janus.plugin.generation;

import de.codecentric.janus.VersionControlSystem;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;

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
        if (catalogFile.trim().isEmpty()) {
            return FormValidation.warning("Please enter a catalog file " +
                    "location.");
        }

        File file = new File(catalogFile);

        if (!file.exists()) {
            return FormValidation.error("The file doesn't exist.");
        } else if (!file.canRead()) {
            return FormValidation.error("The file can't be read " +
                    "(insufficient access rights).");
        } else if (!file.isFile()) {
            return FormValidation.error("The path doesn't denote a file.");
        }

        return FormValidation.ok();
    }

    public FormValidation doCheckScaffoldDirectory(@QueryParameter String scaffoldDirectory) {
        if (scaffoldDirectory.trim().isEmpty()) {
            return FormValidation.warning("Please enter a scaffold directory " +
                    "location.");
        }

        File file = new File(scaffoldDirectory);

        if (!file.exists()) {
            return FormValidation.error("The directory doesn't exist.");
        } else if (!file.canRead()) {
            return FormValidation.error("The directory can't be read " +
                    "(insufficient access rights).");
        } else if (!file.isDirectory()) {
            return FormValidation.error("The path doesn't denote a directory.");
        }

        return FormValidation.ok();
    }
    
    public String getCatalogFile() {
        if (generationConfiguration != null) {
            return generationConfiguration.getCatalogFile();
        }

        return null;
    }

    public String getScaffoldDirectory() {
        if (generationConfiguration != null) {
            return generationConfiguration.getScaffoldDirectory();
        }

        return null;
    }
}
