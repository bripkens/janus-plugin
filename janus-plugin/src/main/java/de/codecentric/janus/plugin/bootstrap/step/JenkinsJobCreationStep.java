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

package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.ci.jenkins.JenkinsConfigGenerator;
import de.codecentric.janus.ci.jenkins.JenkinsProjectCreator;
import de.codecentric.janus.ci.jenkins.conf.ServiceConfig;
import de.codecentric.janus.conf.vcs.VCSConfig;
import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import de.codecentric.janus.scaffold.BuildJob;
import de.codecentric.janus.scaffold.Scaffold;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JenkinsJobCreationStep extends AbstractBootstrapStep {

    private VCSConfig vcsConfig;
    private ServiceConfig serviceConfig;
    private Scaffold scaffold;

    public JenkinsJobCreationStep(ParsedFormData data,
                                  BootstrapLogger logger) {
        super(data, logger);

        vcsConfig = data.getVcsConfiguration().getVcs().newConfig();
        vcsConfig.setUrl(data.getVcsConfiguration().getCheckoutUrl());

        serviceConfig = new ServiceConfig();
        serviceConfig.setUri(data.getCiConfiguration().getUrl());
        serviceConfig.setUsername(data.getCiConfiguration().getUsername());
        serviceConfig.setApiToken(data.getCiConfiguration().getApiToken());

        scaffold = getScaffold();
    }

    @Override
    public boolean execute() {
        logger.log("Generating Jenkins build jobs according to scaffolds");

        getCreator().create();

        logger.log("Successfully added build jobs to Jenkins.",
                LogEntry.Type.SUCCESS);
        return true;
    }

    private JenkinsProjectCreator getCreator() {
        return new JenkinsProjectCreator(serviceConfig,
                data.getProject(),
                vcsConfig,
                scaffold,
                data.getContext());
    }
}
