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

import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.scaffold.Scaffold;
import hudson.model.Build;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractBootstrapStep {
    protected final ParsedFormData data;
    protected final BootstrapLogger logger;
    protected final GenerationConfiguration generationConfig;

    public AbstractBootstrapStep(ParsedFormData data, BootstrapLogger logger) {
        this.data = data;
        this.logger = logger;
        generationConfig = GenerationConfiguration.get();
    }

    public abstract boolean execute();

    protected Future<Build> executeJob(String name, Map<String, String> params) {
        logger.log("Scheduling build job '" + name + "' with parameters " +
                params);
        return JanusPlugin.scheduleBuild(name, params);
    }

    protected Scaffold getScaffold() {
        String scaffoldDir;
        scaffoldDir = ensureFileSeparator(generationConfig.getScaffoldDir());
        String scaffoldFileName = data.getScaffold().getFilename();
        return Scaffold.from(new File(scaffoldDir + scaffoldFileName));
    }

    protected File getOutputDir() {
        String tmpDir = ensureFileSeparator(generationConfig.getTempDir());
        String outputDir = tmpDir + data.getProject().getName();
        return new File(outputDir);
    }

    protected String ensureFileSeparator(String path) {
        if (path.endsWith(File.separator)) {
            return path;
        } else {
            return path + File.separator;
        }
    }
}
