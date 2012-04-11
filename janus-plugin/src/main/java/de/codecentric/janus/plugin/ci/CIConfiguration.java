package de.codecentric.janus.plugin.ci;

import de.codecentric.janus.plugin.Validatable;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.util.FormValidation;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CIConfiguration implements Describable<CIConfiguration>, Validatable {
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(
            new String[] {"http", "https"},
            UrlValidator.ALLOW_LOCAL_URLS
    );

    private String name, url, username, apiToken;

    public CIConfiguration() {
    }

    @DataBoundConstructor
    public CIConfiguration(String name, String url, String username, String apiToken) {
        this.name = name;
        this.url = url;
        this.username = username;
        this.apiToken = apiToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public boolean isValid() {
        FormValidation.Kind ok = FormValidation.Kind.OK;

        if (doCheckName(name).kind != ok) {
            return false;
        } else if (doCheckUrl(url).kind != ok) {
            return false;
        } else if (doCheckUsername(username).kind != ok) {
            return false;
        } else if (doCheckApiToken(apiToken).kind != ok) {
            return false;
        }

        return true;
    }

    public static FormValidation doCheckName(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Please provide a name so that the " +
                    "CI system can be identified on the project bootstrap " +
                    "page.");
        }

        return FormValidation.ok();
    }

    public static FormValidation doCheckUrl(@QueryParameter String value) {
        if (URL_VALIDATOR.isValid(value)) {
            return FormValidation.ok();
        }

        return FormValidation.error("The URL is not a valid URL.");
    }

    public static FormValidation doCheckUsername(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Username must not be empty");
        }

        return FormValidation.ok();
    }

    public static FormValidation doCheckApiToken(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Please provide an API token.");
        }

        return FormValidation.ok();
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
