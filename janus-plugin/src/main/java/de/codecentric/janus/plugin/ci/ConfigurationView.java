package de.codecentric.janus.plugin.ci;

import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import hudson.Extension;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class ConfigurationView extends Descriptor<CIConfiguration> {

    private static final String CONFIGURATION_FIELD_NAME = "configurations";

    private CIConfiguration[] configurations;

    public ConfigurationView() {
        super(CIConfiguration.class);
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
        configurations = req.bindJSONToList(CIConfiguration.class, data)
                .toArray(new CIConfiguration[]{});
        save();
        return super.configure(req, formData);
    }


    public CIConfiguration[] getConfigurations() {
        return configurations;
    }

    public void setConfigurations(CIConfiguration... configurations) {
        this.configurations = configurations;
        save();
    }
}
