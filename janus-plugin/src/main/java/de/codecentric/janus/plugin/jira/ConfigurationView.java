package de.codecentric.janus.plugin.jira;

import de.codecentric.janus.plugin.ci.CIConfiguration;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class ConfigurationView extends Descriptor<JiraConfiguration>  {
    private static final String CONFIGURATION_FIELD_NAME = "configurations";

    private JiraConfiguration[] configurations = new JiraConfiguration[0];

    public ConfigurationView() {
        super(JiraConfiguration.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Janus Generation Configuration";
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData)
            throws FormException {
        Object data = formData.get(CONFIGURATION_FIELD_NAME);
        configurations = req.bindJSONToList(JiraConfiguration.class, data)
                .toArray(new JiraConfiguration[]{});
        save();
        return super.configure(req, formData);
    }

    public FormValidation doCheckName(@QueryParameter String value) {
        return JiraConfiguration.doCheckName(value);
    }

    public FormValidation doCheckJiraUrl(@QueryParameter String value) {
        return JiraConfiguration.doCheckJiraUrl(value);
    }

    public FormValidation doCheckConfluenceUrl(@QueryParameter String value) {
        return JiraConfiguration.doCheckConfluenceUrl(value);
    }

    public FormValidation doCheckUsername(@QueryParameter String value) {
        return JiraConfiguration.doCheckUsername(value);
    }

    public FormValidation doCheckPassword(@QueryParameter String value) {
        return JiraConfiguration.doCheckPassword(value);
    }

    public JiraConfiguration[] getConfigurations() {
        return configurations;
    }

    public void setConfigurations(JiraConfiguration... configurations) {
        this.configurations = configurations;
        save();
    }
}
