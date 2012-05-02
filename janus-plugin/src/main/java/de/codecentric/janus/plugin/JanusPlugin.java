package de.codecentric.janus.plugin;

import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.generation.GenerationConfiguration;
import de.codecentric.janus.plugin.jira.JiraConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import hudson.model.*;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
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
        List<VCSConfiguration> vcsConfigs = new LinkedList<VCSConfiguration>();

        for(VCSConfiguration vcsConfig : getVCSConfigs()) {
            if (vcsConfig.isValid()) {
                vcsConfigs.add(vcsConfig);
            }
        }

        return vcsConfigs.toArray(new VCSConfiguration[]{});
    }

    public static CIConfiguration[] getCIConfigs() {
        return CIConfiguration.get();
    }

    public static CIConfiguration[] getValidCIConfigs() {
        List<CIConfiguration> ciConfigs = new LinkedList<CIConfiguration>();

        for(CIConfiguration ciConfig : getCIConfigs()) {
            if (ciConfig.isValid()) {
                ciConfigs.add(ciConfig);
            }
        }

        return ciConfigs.toArray(new CIConfiguration[]{});
    }

    public static GenerationConfiguration getGenerationConfig() {
        return GenerationConfiguration.get();
    }

    public static JiraConfiguration[] getJiraConfigurations() {
        return JiraConfiguration.get();
    }

    public static JiraConfiguration[] getValidJiraConfigurations() {
        List<JiraConfiguration> jiraConfigs = new LinkedList<JiraConfiguration>();

        for (JiraConfiguration jiraConfig : getJiraConfigurations()) {
            if (jiraConfig.isValid()) {
                jiraConfigs.add(jiraConfig);
            }
        }

        return jiraConfigs.toArray(new JiraConfiguration[]{});
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

        CIConfiguration[] ciConfigs = getValidCIConfigs();
        if (ciConfigs.length == 0) {
            LOGGER.info("Jenkins is not properly configured for project " +
                    "bootstrap because no valid CI config exists.");
            return false;
        }

        JiraConfiguration[] jiraConfigs = getValidJiraConfigurations();
        if (jiraConfigs.length == 0) {
            LOGGER.info("Jenkins is not properly configured for project " +
                    "bootstrap because no valid JIRA config exists.");
            return false;
        }

        return true;
    }

    public static boolean isValid(Validatable[] validatables) {
        for (Validatable validatable : validatables) {
            if (!validatable.isValid()) {
                return false;
            }
        }

        return true;
    }
    
    public static Future<Build> scheduleBuild(String jobName,
                                              Map<String, String> parameters) {
        Project job = getJob(jobName);

        Cause buildCause = new Cause.UserCause();

        List<ParameterValue> hudsonBuildParameters;
        hudsonBuildParameters = toHudsonJobParameters(parameters);
        ParametersAction action = new ParametersAction(hudsonBuildParameters);

        return job.scheduleBuild2(0, buildCause, action);
    }

    public static Project getJob(String jobName) {
        for(Project p : Hudson.getInstance().getProjects()) {
            if (p.getName().equals(jobName)) {
                return p;
            }
        }

        throw new IllegalArgumentException("Build job '" + jobName + "' " +
                "doesn't exist");
    }

    public static List<ParameterValue> toHudsonJobParameters(
            Map<String, String> parameters) {

        List<ParameterValue> hudsonParameters;
        hudsonParameters = new ArrayList<ParameterValue>(parameters.size());

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            ParameterValue parameter = new StringParameterValue(entry.getKey(),
                    entry.getValue());
            hudsonParameters.add(parameter);
        }

        return hudsonParameters;
    }
}
