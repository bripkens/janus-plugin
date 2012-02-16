package de.codecentric.janus.plugin;

import de.codecentric.janus.VersionControlSystem;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Collection;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public final class Configuration extends Descriptor<NewProjectAction> {
    private String scaffoldDirectory, catalogFile, addCommand,
            checkoutCommand, commitCommand, buildParameter, buildJob;
    private VersionControlSystem vcs;

    public Configuration() {
        super(NewProjectAction.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Janus configuration";
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData)
            throws FormException {
        System.out.println(formData.toString());
        scaffoldDirectory = formData.getString("scaffoldDirectory");
        catalogFile = formData.getString("catalogFile");
        addCommand = formData.getString("addCommand");
        checkoutCommand = formData.getString("checkoutCommand");
        commitCommand = formData.getString("commitCommand");
        buildParameter = formData.getString("buildParameter");
        buildJob = formData.getString("buildJob");
        vcs = VersionControlSystem.valueOf(formData.getString("vcs"));

        save();

        return super.configure(req, formData);
    }

    public String getScaffoldDirectory() {
        return scaffoldDirectory;
    }

    public String getCatalogFile() {
        return catalogFile;
    }

    public String getAddCommand() {
        return addCommand;
    }

    public void setAddCommand(String addCommand) {
        this.addCommand = addCommand;
    }

    public String getCheckoutCommand() {
        return checkoutCommand;
    }

    public void setCheckoutCommand(String checkoutCommand) {
        this.checkoutCommand = checkoutCommand;
    }

    public String getCommitCommand() {
        return commitCommand;
    }

    public void setCommitCommand(String commitCommand) {
        this.commitCommand = commitCommand;
    }

    public String getBuildParameter() {
        return buildParameter;
    }

    public ListBoxModel doFillBuildJobItems() {
        ListBoxModel m = new ListBoxModel();
        m.add("Please select", "");

        Collection<String> allJobs = Hudson.getInstance().getJobNames();
        for (String job : allJobs) {
            if (buildJob != null && job.equals(buildJob)) {
                m.add(new ListBoxModel.Option(job, job, true));
            } else {
                m.add(job);
            }
        }

        return m;
    }

    public ListBoxModel doFillVcsItems() {
        ListBoxModel m = new ListBoxModel();
        m.add("Please select", "");

        for (VersionControlSystem vcs : VersionControlSystem.values()) {
            if (this.vcs == vcs) {
                m.add(new ListBoxModel.Option(vcs.name(), vcs.name(),
                        true));
            } else {
                m.add(vcs.name());
            }

        }

        return m;
    }

    public FormValidation doCheckAddCommand(@QueryParameter String value) {
        return validatePresence(value);
    }

    private FormValidation validatePresence(String value) {
        return validatePresence(value, "Please provide a command.");
    }

    private FormValidation validatePresence(String value, String msg) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.warning(msg);
        } else {
            return FormValidation.ok();
        }
    }

    public FormValidation doCheckCheckoutCommand(@QueryParameter String value) {
        return validatePresence(value);
    }

    public FormValidation doCheckCommitCommand(@QueryParameter String value) {
        return validatePresence(value);
    }

    public FormValidation doCheckBuildParameter(@QueryParameter String value) {
        return validatePresence(value, "Please define the parameter.");
    }

    public FormValidation doCheckVcs(@QueryParameter String value) {
        if (value.trim().isEmpty()) {
            return FormValidation.warning("Please select one of the " +
                    "Version Control Systems.");
        }

        try {
            VersionControlSystem.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return FormValidation.error("This Version Control " +
                    "System is not supported.");
        }

        return FormValidation.ok();

    }

    public FormValidation doCheckBuildJob(@QueryParameter String value) {
        if (value.trim().isEmpty()) {
            return FormValidation.warning("Please select one of the " +
                    "build jobs.");
        }

        if (Hudson.getInstance().getJobNames().contains(value)) {
            return FormValidation.ok();
        } else {
            return FormValidation.error("This build job doesn't exist.");
        }
    }
}
