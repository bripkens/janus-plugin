package de.codecentric.janus.plugin.generation;

import hudson.model.Describable;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class GenerationConfiguration implements Describable<GenerationConfiguration> {
    private String catalogFile, scaffoldDirectory, tempDirectory;

    public GenerationConfiguration() {
    }

    @DataBoundConstructor
    public GenerationConfiguration(String catalogFile, String scaffoldDirectory, String tempDirectory) {
        this.catalogFile = catalogFile;
        this.scaffoldDirectory = scaffoldDirectory;
        this.tempDirectory = tempDirectory;
    }

    public ConfigurationView getDescriptor() {
        return (ConfigurationView) Hudson
                .getInstance()
                .getDescriptor(GenerationConfiguration.class);
    }

    public boolean isValid() {
        return doCheckCatalogFile(catalogFile).kind == FormValidation.Kind.OK &&
                doCheckScaffoldDirectory(scaffoldDirectory).kind == FormValidation.Kind.OK;
    }

    public static FormValidation doCheckCatalogFile(String catalogFile) {
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

    public static FormValidation doCheckScaffoldDirectory(String scaffoldDirectory) {
        return doCheckDirectory(scaffoldDirectory);
    }
    
    public static FormValidation doCheckTempDirectory(String tempDirectory) {
        FormValidation dirCheck = doCheckDirectory(tempDirectory);

        if (dirCheck.kind != FormValidation.Kind.OK) {
            return dirCheck;
        }

        File dir = new File(tempDirectory);
        
        if (dir.canWrite()) {
            return FormValidation.ok();
        } else {
            return FormValidation.error("Can't write to directory.");
        }
    }
    
    private static FormValidation doCheckDirectory(String dir) {
        if (dir.trim().isEmpty()) {
            return FormValidation.warning("Please enter a scaffold directory " +
                    "location.");
        }

        File file = new File(dir);

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
        return catalogFile;
    }

    public void setCatalogFile(String catalogFile) {
        this.catalogFile = catalogFile;
    }

    public String getScaffoldDirectory() {
        return scaffoldDirectory;
    }

    public void setScaffoldDirectory(String scaffoldDirectory) {
        this.scaffoldDirectory = scaffoldDirectory;
    }

    public static GenerationConfiguration get() {
        return new ConfigurationView().getGenerationConfiguration();
    }

    public String getTempDirectory() {
        return tempDirectory;
    }

    public void setTempDirectory(String tempDirectory) {
        this.tempDirectory = tempDirectory;
    }
}
