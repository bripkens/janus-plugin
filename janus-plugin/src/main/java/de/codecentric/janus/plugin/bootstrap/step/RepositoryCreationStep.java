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

import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.JanusPluginBootstrapException;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import hudson.model.Build;
import hudson.model.Result;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCreationStep extends AbstractBuildDependentStep {

    public RepositoryCreationStep(ParsedFormData data,
                                  BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    protected String beforeMessage() {
        return "Creating repository.";
    }

    @Override
    protected String successMessage() {
        return "Repository creation successful.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Repository creation build job failed. Please see build" +
                "job '" + getBuildJobName() + "' for more information (" +
                buildUrl + ").";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getGenerationBuildJob();
    }
}
