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
import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import de.codecentric.janus.scaffold.Scaffold;
import de.codecentric.janus.generation.ScaffoldExecutor;

import java.io.File;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SourceCodeGenerationStep extends AbstractBootstrapStep {

    public SourceCodeGenerationStep(ParsedFormData data,
                                    BootstrapLogger logger) {
        super(data, logger);
    }

    @Override
    public boolean execute() {
        logger.log("Generating source code according to scaffold.");

        Scaffold scaffold = getScaffold();
        File output = getOutputDir();

        ScaffoldExecutor executor;
        executor = new ScaffoldExecutor(scaffold,
                data.getProject(),
                data.getContext());

        executor.apply(output);

        logger.log("Successfully generated sources in target directory.",
                LogEntry.Type.SUCCESS);

        return true;
    }
}
