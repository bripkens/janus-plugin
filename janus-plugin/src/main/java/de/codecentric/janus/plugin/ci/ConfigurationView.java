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

package de.codecentric.janus.plugin.ci;

import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class ConfigurationView extends Descriptor<CIConfiguration> {

    private static final String CONFIGURATION_FIELD_NAME = "configurations";

    private CIConfiguration[] configurations = new CIConfiguration[0];

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

    public FormValidation doCheckName(@QueryParameter String value) {
        return CIConfiguration.doCheckName(value);
    }

    public FormValidation doCheckUrl(@QueryParameter String value) {
        return CIConfiguration.doCheckUrl(value);
    }

    public FormValidation doCheckUsername(@QueryParameter String value) {
        return CIConfiguration.doCheckUsername(value);
    }

    public FormValidation doCheckApiToken(@QueryParameter String value) {
        return CIConfiguration.doCheckApiToken(value);
    }

    public CIConfiguration[] getConfigurations() {
        return configurations;
    }

    public void setConfigurations(CIConfiguration... configurations) {
        this.configurations = configurations;
        save();
    }
}
