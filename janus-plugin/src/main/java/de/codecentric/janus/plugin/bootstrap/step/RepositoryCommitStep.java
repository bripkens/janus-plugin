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
public class RepositoryCommitStep extends AbstractBuildDependentStep {

    public RepositoryCommitStep(ParsedFormData data,
                                BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    protected String beforeMessage() {
        return "Commiting changes to (remote) repository.";
    }

    @Override
    protected String successMessage() {
        return "Successfully commited all changes.";
    }

    @Override
    protected String failMessage(String buildUrl) {
        return "Failed to commit changes. Build job " + getBuildJobName() +
                "failed. See " + buildUrl + " for details.";
    }

    @Override
    protected String getBuildJobName() {
        return data.getVcsConfiguration().getCommitBuildJob();
    }
}
