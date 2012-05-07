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

package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.JanusPluginException;
import hudson.Extension;
import hudson.model.*;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.acegisecurity.AccessDeniedException;
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
public class NewRepositoryAction implements RootAction, AccessControlled {
    public static final String URL = "new-repository";

    private static final Logger LOGGER = Logger
            .getLogger(NewRepositoryAction.class.getName());

    public static final Permission PERMISSION = JanusPlugin.CREATE_REPOSITORY;

    public String getIconFileName() {
        // returning null hides the menu item
        if (Hudson.getInstance().hasPermission(PERMISSION)) {
            return "new-package.png";
        } else {
            return null;
        }
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

    public boolean isAtLeastOneVCSConfigured() {
        VCSConfiguration[] configurations;
        configurations = new ConfigurationView().getConfigurations();
        return configurations != null && configurations.length > 0;
    }

    public void doSubmit(StaplerRequest req, StaplerResponse rsp)
            throws ServletException, IOException {
        checkPermission(PERMISSION);
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
            throw new JanusPluginException(e);
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while creating" +
                    "repository.", e);
            throw new JanusPluginException(e);
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
        if (name.equals(RepositoryCreationSuccessAction.URL)) {
            return new RepositoryCreationSuccessAction();
        }

        return null;
    }

    public ACL getACL() {
        return Jenkins.getInstance()
                .getAuthorizationStrategy()
                .getACL(Jenkins.getInstance().getComputers()[0]);
    }

    public void checkPermission(Permission p) throws AccessDeniedException {
        getACL().checkPermission(p);
    }

    public boolean hasPermission(Permission p) {
        return getACL().hasPermission(p);
    }
}
