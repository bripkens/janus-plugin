package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.CatalogEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BootstrapExecutor {

    private static final Logger LOGGER = Logger
            .getLogger(BootstrapExecutor.class.getName());

    private final AtomicBoolean atomicBoolean;

    private final Project project;
    private final VCSConfiguration vcsConfiguration;
    private final CatalogEntry catalogEntry;
    private final Map<String, String> context;

    private List<String> log;

    BootstrapExecutor(Project project, VCSConfiguration vcsConfiguration,
                      CatalogEntry catalogEntry, Map<String, String> context) {
        this.project = project;
        this.vcsConfiguration = vcsConfiguration;
        this.catalogEntry = catalogEntry;
        this.context = context;

        atomicBoolean = new AtomicBoolean();
        log = new LinkedList<String>();
    }

    List<String> execute() {
        if (!atomicBoolean.compareAndSet(false, true)) {
            throw new IllegalStateException("A bootstrap executor may only " +
                    "be used once. Please create a new instance.");
        }

        log("Starting project bootstrap for project '" +
                project.getName() + "'.");
        
        createRepository();
        checkoutRepository();
        generateSources();
        commitChanges();

        log("Finished project bootstrap for project '" +
                project.getName() + "'.");

        return log;
    }

    private void createRepository() {

    }

    private void checkoutRepository() {

    }

    private void generateSources() {

    }

    private void commitChanges() {

    }

    private void log(String msg) {
        LOGGER.info(msg);
        log.add(msg);
    }
}
