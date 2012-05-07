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
        Object data = formData.get(CONFIGURATION_FIELD_NAME);
        configurations = req.bindJSONToList(VCSConfiguration.class, data)
                .toArray(new VCSConfiguration[]{});
        save();
        return super.configure(req, formData);
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

    public FormValidation doCheckName(@QueryParameter String value) {
        return VCSConfiguration.doCheckName(value);
    }

    public FormValidation doCheckCheckoutUrl(@QueryParameter String value) {
        return VCSConfiguration.doCheckCheckoutUrl(value);
    }

    public FormValidation doCheckVcs(@QueryParameter String value) {
       return VCSConfiguration.doCheckVcs(value);
    }

    public FormValidation doCheckGenerationBuildJob(@QueryParameter String value) {
        return VCSConfiguration.doCheckBuildJob(value);
    }

    public FormValidation doCheckCheckoutBuildJob(@QueryParameter String value) {
        return VCSConfiguration.doCheckBuildJob(value);
    }

    public FormValidation doCheckCommitBuildJob(@QueryParameter String value) {
        return VCSConfiguration.doCheckBuildJob(value);
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
