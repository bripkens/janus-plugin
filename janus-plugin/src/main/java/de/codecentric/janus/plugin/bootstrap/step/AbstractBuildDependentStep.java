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
import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import hudson.model.Build;
import hudson.model.Result;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBuildDependentStep extends AbstractBootstrapStep {

    public AbstractBuildDependentStep(ParsedFormData data,
                                      BootstrapLogger logger) {
        super(data, logger);
    }

    protected abstract String beforeMessage();
    protected abstract String successMessage();
    protected abstract String failMessage(String buildUrl);
    protected abstract String getBuildJobName();
    
    public boolean execute() {
        logger.log(beforeMessage());

        String buildJob = getBuildJobName();

        Future<Build> scheduledBuild;
        scheduledBuild = executeJob(buildJob,
                new HashMap<String, String>() {{
                    put("name", data.getProject().getName());
                    put("tmpDir", generationConfig.getTempDir());
                }});


        Build executedBuild;
        try {
            executedBuild = scheduledBuild.get();
        } catch (InterruptedException e) {
            throw new JanusPluginBootstrapException(e);
        } catch (ExecutionException e) {
            throw new JanusPluginBootstrapException(e);
        }

        if (!executedBuild.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
            logger.log(failMessage(executedBuild.getUrl()),
                    LogEntry.Type.FAILURE);
            return false;
        }

        logger.log(successMessage(), LogEntry.Type.SUCCESS);
        return true;
    }
}
