/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.janus.plugin.jira;

import de.codecentric.janus.plugin.Validatable;
import de.codecentric.janus.plugin.ci.*;
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
public class JiraConfiguration implements Describable<JiraConfiguration>, Validatable {
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(
            new String[] {"http", "https"},
            UrlValidator.ALLOW_LOCAL_URLS
    );

    private String name, jiraUrl, confluenceUrl, username, password;

    public JiraConfiguration() {
    }

    @DataBoundConstructor
    public JiraConfiguration(String name, String jiraUrl, String confluenceUrl,
                             String username, String password,
                             boolean sharedUserDatabase) {
        this.name = name;
        this.jiraUrl = jiraUrl;
        this.confluenceUrl = confluenceUrl;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJiraUrl() {
        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }

    public String getConfluenceUrl() {
        return confluenceUrl;
    }

    public void setConfluenceUrl(String confluenceUrl) {
        this.confluenceUrl = confluenceUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        FormValidation.Kind ok = FormValidation.Kind.OK;

        if (doCheckName(name).kind != ok) {
            return false;
        } else if (doCheckJiraUrl(jiraUrl).kind != ok) {
            return false;
        } else if (doCheckConfluenceUrl(confluenceUrl).kind != ok) {
            return false;
        } else if (doCheckUsername(username).kind != ok) {
            return false;
        } else if (doCheckPassword(password).kind != ok) {
            return false;
        }

        return true;
    }

    public static FormValidation doCheckName(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Please provide a name so that the " +
                    "systems can be identified on the project bootstrap " +
                    "page.");
        }

        return FormValidation.ok();
    }

    private static FormValidation doCheckUrl(@QueryParameter String value) {
        if (URL_VALIDATOR.isValid(value)) {
            return FormValidation.ok();
        }

        return FormValidation.error("The URL is not a valid URL.");
    }

    public static FormValidation doCheckJiraUrl(@QueryParameter String value) {
        return doCheckUrl(value);
    }

    public static FormValidation doCheckConfluenceUrl(@QueryParameter String value) {
        return doCheckUrl(value);
    }

    public static FormValidation doCheckUsername(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Username must not be empty");
        }

        return FormValidation.ok();
    }

    public static FormValidation doCheckPassword(@QueryParameter String value) {
        if (GenericValidator.isBlankOrNull(value)) {
            return FormValidation.error("Please provide a password.");
        }

        return FormValidation.ok();
    }

    public static JiraConfiguration[] get() {
        JiraConfiguration[] configs;
        configs = new ConfigurationView().getConfigurations();

        if (configs == null) {
            configs = new JiraConfiguration[]{};
        }

        return configs;
    }

    public Descriptor<JiraConfiguration> getDescriptor() {
        return (Descriptor<JiraConfiguration>) Hudson
                .getInstance()
                .getDescriptor(JiraConfiguration.class);
    }
}
