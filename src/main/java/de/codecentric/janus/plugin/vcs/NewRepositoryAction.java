package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.plugin.JanusPluginException;
import de.codecentric.janus.plugin.JanusPluginGenerationException;
import hudson.Extension;
import hudson.model.*;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class NewRepositoryAction implements RootAction {
    private static final Logger LOGGER = Logger
            .getLogger(NewRepositoryAction.class.getName());
    public static final String URL = "new-repository";

    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New repository";
    }

    public String getUrlName() {
        return URL;
    }

    public VCSConfiguration[] getConfigurations() {
        return new ConfigurationView().getConfigurations();
    }

    public void doSubmit(StaplerRequest req, StaplerResponse rsp)
            throws ServletException, IOException {
        JSONObject submittedForm = req.getSubmittedForm();

        String repositoryName = submittedForm.getString("name");
        VCSConfiguration config = getByName(submittedForm.getString(""));

        Project p = getProject(config.getGenerationBuildJob());
        Cause buildCause = new Cause.UserCause();
        ParameterValue parameter = new StringParameterValue("NAME",
                repositoryName);

        Future<Build> f = p.scheduleBuild2(0,
                buildCause,
                new ParametersAction(parameter));

        try {
            Build build = f.get();

            if (build.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
                LOGGER.log(Level.INFO,
                        "Successfully created repository {0}.",
                        repositoryName);
                // Adding a slash because the getUrl() method returns a relative
                // URL and we are currently at /new-repository/.
                rsp.sendRedirect("/" +
                        getUrlName() +
                        "/" +
                        RepositoryCreationSuccessAction.URL +
                        "?checkoutBuild=" +
                        config.getCheckoutBuildJob());
            } else {
                LOGGER.log(Level.WARNING,
                        "Repository creation failed for name {0} and " +
                                "VCS configuration {1}.",
                        new String[]{repositoryName, config.getName()});

                req.setAttribute("name", repositoryName);
                req.setAttribute("selectedVCS", config.getName());
                req.setAttribute("buildURL", "/" + build.getUrl());
                req.setAttribute("error", true);

                // leading and training slash as Jenkins otherwise issues an
                // HTTP status code 301 (moved permanently), which would then
                // result in the loss of the request attributes.

                // rsp.forwardToPreviousPage() can't be used as this method
                // loses the request attributes due to a HTTP redirect instead
                // of forward

                req.getRequestDispatcher("/" + URL + "/").forward(req, rsp);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Got interrupted while waiting " +
                    "on repository build to finish.", e);
            throw new JanusPluginGenerationException(e);
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while creating" +
                    "repository.", e);
            throw new JanusPluginGenerationException(e);
        }
    }
    
    private VCSConfiguration getByName(String name) {
        for(VCSConfiguration config : getConfigurations()) {
            if (config.getName().equals(name)) {
                return config;
            }
        }

        return null;
    }
    
    private Project getProject(String name) {
        for(Project p : Hudson.getInstance().getProjects()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        throw new JanusPluginException("Project / build job with " +
                "name " + name + " doesn't exist.");
    }

    public Action getDynamic(String name) {
        if (name.equals("success")) {
            return new RepositoryCreationSuccessAction();
        }

        return null;

    }
}