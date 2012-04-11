package de.codecentric.janus.plugin.bootstrap.step;

import de.codecentric.janus.plugin.bootstrap.LogEntry;
import de.codecentric.janus.scaffold.Scaffold;
import de.codecentric.janus.generation.ScaffoldExecutor;

import java.io.File;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SourceCodeGenerationStep extends AbstractBootstrapStep {

    public SourceCodeGenerationStep(StepExecutionData data) {
        super(data);
    }

    @Override
    public boolean execute() {
        data.log("Generating source code according to scaffold.");

        Scaffold scaffold = getScaffold();
        File output = getOutputDir();

        ScaffoldExecutor executor;
        executor = new ScaffoldExecutor(scaffold,
                data.getProject(),
                data.getContext());

        executor.apply(output);

        data.log("Successfully generated sources in target directory.",
                LogEntry.Type.SUCCESS);

        return true;
    }
}
