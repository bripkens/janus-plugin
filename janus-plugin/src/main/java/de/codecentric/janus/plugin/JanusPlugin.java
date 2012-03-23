package de.codecentric.janus.plugin;

import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusPlugin {

    public static PermissionGroup PERMISSIONS = new PermissionGroup(
            JanusPlugin.class, Messages._permissions_group_title());

    public static Permission CREATE_REPOSITORY = new Permission(PERMISSIONS,
            "CreateRepository",
            null,
            Permission.CREATE,
            PermissionScope.COMPUTER);

    public static Permission BOOTSTRAP_PROJECT = new Permission(PERMISSIONS,
            "BootstrapProject",
            null,
            Permission.CREATE,
            PermissionScope.COMPUTER);

    private static final Logger LOGGER = Logger
            .getLogger(JanusPlugin.class.getName());


    public JanusPlugin() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static VCSConfiguration[] getVCSConfigs() {
        return VCSConfiguration.get();
    }

    public static VCSConfiguration[] getValidVCSConfigs() {
        List<VCSConfiguration> vcsConfigs = new ArrayList<VCSConfiguration>();

        for(VCSConfiguration vcsConfig : getVCSConfigs()) {
            if (vcsConfig.isValid()) {
                vcsConfigs.add(vcsConfig);
            }
        }

        return vcsConfigs.toArray(new VCSConfiguration[]{});
    }

    public static GenerationConfiguration getGenerationConfig() {
        return GenerationConfiguration.get();
    }

    public static Catalog getCatalog() {
        return Catalog.from(new File(getGenerationConfig().getCatalogFile()));
    }

    public static boolean isJenkinsConfiguredForProjectBootstrap() {
        VCSConfiguration[] vcsConfigs = getValidVCSConfigs();
        if (vcsConfigs.length == 0) {
            LOGGER.info("Jenkins is not properly configured for project " +
                    "bootstrap because no valid VCS config exists.");
            return false;
        }

        GenerationConfiguration genConfig = GenerationConfiguration.get();
        if (genConfig == null || !genConfig.isValid()) {
            LOGGER.info("Jenkins is not properly configured for project " +
                    "bootstrap because no source code generation " +
                    "config exists (catalog file and scaffold directory " +
                    "location.");
            return false;
        }

        return true;
    }
}
