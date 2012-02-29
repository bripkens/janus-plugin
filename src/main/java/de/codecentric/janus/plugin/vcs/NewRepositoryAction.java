package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.plugin.JanusPluginException;
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

    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New repository";
    }

    public String getUrlName() {
        return "new-repository";
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
                LOGGER.info("Yeah, success!");
                // Adding a slash because the getUrl() method returns a relative
                // URL and we are currently at /new-repository/.
                rsp.sendRedirect("/" + build.getUrl());
            } else {
                rsp.forwardToPreviousPage(req);
                LOGGER.info("Oh noes, failure!");
            }


        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Got interrupted while waiting " +
                    "on repository build to finish.", e);
        } catch (ExecutionException e) {
//            req.setAttribute();
            rsp.forwardToPreviousPage(req);
            LOGGER.log(Level.WARNING, "Error occurred while creating" +
                    "repository.", e);
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

    public FormValidation doCheckName(@QueryParameter String value) {
        return validatePresence(value, "Please provide a valid name.");
    }

    private FormValidation validatePresence(String value, String msg) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.error(msg);
        } else {
            return FormValidation.ok();
        }
    }
}
