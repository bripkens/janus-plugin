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

    private Scaffold getScaffold() {
        String scaffoldDir;
        scaffoldDir = ensureFileSeparator(generationConfig.getScaffoldDir());
        String scaffoldFileName = data.getCatalogEntry().getFilename();
        return Scaffold.from(new File(scaffoldDir + scaffoldFileName));
    }
    
    private File getOutputDir() {
        String tmpDir = ensureFileSeparator(generationConfig.getTempDir());
        String outputDir = tmpDir + data.getProject().getName();
        return new File(outputDir);
    }
    
    private String ensureFileSeparator(String path) {
        if (path.endsWith(File.separator)) {
            return path;
        } else {
            return path + File.separator;
        }
    }
}
