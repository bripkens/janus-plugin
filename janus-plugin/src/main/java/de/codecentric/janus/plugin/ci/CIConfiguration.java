package de.codecentric.janus.plugin.ci;

import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CIConfiguration implements Describable<CIConfiguration> {
    private String baseUrl, username, apiToken;

    public CIConfiguration() {
    }

    @DataBoundConstructor
    public CIConfiguration(String baseUrl, String username, String apiToken) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.apiToken = apiToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public static CIConfiguration[] get() {
        CIConfiguration[] configs;
        configs = new ConfigurationView().getConfigurations();

        if (configs == null) {
            configs = new CIConfiguration[]{};
        }

        return configs;
    }

    public Descriptor<CIConfiguration> getDescriptor() {
        return (Descriptor<CIConfiguration>) Hudson
                .getInstance()
                .getDescriptor(CIConfiguration.class);
    }
}
