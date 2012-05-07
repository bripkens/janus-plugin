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
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCheckoutStep extends AbstractBuildDependentStep {

    public RepositoryCheckoutStep(ParsedFormData data,
                                  BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    protected String beforeMessage() {
        return "Checking out repository";
    }

    @Override
    protected String successMessage() {
        return "Successfully checked out repository into temporary directory.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Failed to check out the repository. Please see build " +
                getBuildJobName() + " for details (" + buildUrl + ").";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getCheckoutBuildJob();
    }
}
