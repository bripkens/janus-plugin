package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.VersionControlSystem;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Collection;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class ConfigurationView extends Descriptor<VCSConfiguration> {

    private static final String CONFIGURATION_FIELD_NAME = "configurations";
    
    private VCSConfiguration[] configurations = new VCSConfiguration[0];

    public ConfigurationView() {
        super(VCSConfiguration.class);
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData)
            throws FormException {
        configurations = parseData(formData);
        save();
        return super.configure(req, formData);
    }

    private VCSConfiguration[] parseData(JSONObject data) {
        try {
            JSONArray array = data.getJSONArray(CONFIGURATION_FIELD_NAME);
            return parseMultiple(array);
        } catch (JSONException ex) {
            JSONObject singleData;
            singleData = data.getJSONObject(CONFIGURATION_FIELD_NAME);
            return new VCSConfiguration[]{ parseSingle(singleData) };
        }
    }

    private VCSConfiguration[] parseMultiple(JSONArray formData) {
        VCSConfiguration[] parsedData = new VCSConfiguration[formData.size()];
        
        for(int i = 0; i < formData.size(); i++) {
            parsedData[i] = parseSingle(formData.getJSONObject(i));
        }
        
        return parsedData;
    }

    private VCSConfiguration parseSingle(JSONObject data) {
        VCSConfiguration config = new VCSConfiguration();
        
        config.setName(data.getString("name"));
        config.setVcs(VersionControlSystem.valueOf(data.getString("vcs")));
        config.setCheckoutBuildJob(data.getString("checkoutBuildJob"));
        config.setCommitBuildJob(data.getString("commitBuildJob"));
        config.setGenerationBuildJob(data.getString("generationBuildJob"));
        
        return config;
    }

    public ListBoxModel doFillVcsItems() {
        ListBoxModel m = new ListBoxModel();
        m.add("Please select", "");

        for (VersionControlSystem vcs : VersionControlSystem.values()) {
            m.add(vcs.name());
        }

        return m;
    }

    private ListBoxModel doFillBuildJobItems() {
        ListBoxModel m = new ListBoxModel();
        m.add("Please select", "");

        Collection<String> allJobs = Hudson.getInstance().getJobNames();
        for (String job : allJobs) {
            m.add(job);
        }

        return m;
    }

    public FormValidation doCheckVcs(@QueryParameter String value) {
        if (value.trim().isEmpty()) {
            return FormValidation.error("Please select one of the " +
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
            return FormValidation.error("Please select one of the " +
                    "build jobs.");
        }

        if (Hudson.getInstance().getJobNames().contains(value)) {
            return FormValidation.ok();
        } else {
            return FormValidation.error("This build job doesn't exist.");
        }
    }
    
    public FormValidation doCheckGenerationBuildJob(@QueryParameter String value) {
        return doCheckBuildJob(value);
    }

    public FormValidation doCheckCheckoutBuildJob(@QueryParameter String value) {
        return doCheckBuildJob(value);
    }

    public FormValidation doCheckCommitBuildJob(@QueryParameter String value) {
        return doCheckBuildJob(value);
    }

    public ListBoxModel doFillCommitBuildJobItems() {
        return doFillBuildJobItems();
    }

    public ListBoxModel doFillGenerationBuildJobItems() {
        return doFillBuildJobItems();
    }

    public ListBoxModel doFillCheckoutBuildJobItems() {
        return doFillBuildJobItems();
    }

    public VCSConfiguration[] getConfigurations() {
        return configurations;
    }

    public void setConfigurations(VCSConfiguration... configurations) {
        this.configurations = configurations;
        save();
    }

    @Override
    public String getDisplayName() {
        return "Janus Version Control System configuration";
    }
}
