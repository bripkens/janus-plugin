package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.RootAction;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import org.acegisecurity.AccessDeniedException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class BootstrapProjectAction implements RootAction, AccessControlled {
    public static final String URL = "bootstrap-project";

    private static final Logger LOGGER = Logger
            .getLogger(BootstrapProjectAction.class.getName());

    public static final Permission PERMISSION = JanusPlugin.BOOTSTRAP_PROJECT;

    /*
    * ############################
    * Jenkins root action configuration
    * ############################
    */
    public String getIconFileName() {
        // returning null hides the menu item
        if (Hudson.getInstance().hasPermission(PERMISSION)) {
            return "new-package.png";
        } else {
            return null;
        }
    }

    public String getDisplayName() {
        return "Bootstrap project";
    }

    public String getUrlName() {
        return URL;
    }

    /*
    * ############################
    * Form events
    * ############################
    */
    public void doSubmit(StaplerRequest req, StaplerResponse rsp)
            throws ServletException, IOException {
        checkPermission(PERMISSION);

        FormData formData = FormData.parse(req.getSubmittedForm());

        if (!formData.isValid()) {
            req.setAttribute("error", true);
            formData.setFormDataAsAttributesOn(req);
            // leading and training slash as Jenkins otherwise issues an
            // HTTP status code 301 (moved permanently), which would then
            // result in the loss of the request attributes.
            req.getRequestDispatcher("/" + URL + "/").forward(req, rsp);
        }
    }

    @JavaScriptMethod
    public boolean isValidName(String value) {
        return Project.isValidName(value);
    }

    @JavaScriptMethod
    public boolean isValidPckg(String value) {
        return Project.isValidPackage(value);
    }

    @JavaScriptMethod
    public boolean isValidDescription(String value) {
        return Project.isValidDescription(value);
    }

    /*
     * ############################
     * Getter for view
     * ############################
     */
    public VCSConfiguration[] getValidVCSConfigs() {
        return JanusPlugin.getValidVCSConfigs();
    }

    public Catalog getCatalog() {
        return JanusPlugin.getCatalog();
    }

    public boolean isJenkinsConfigured() {
        return JanusPlugin.isJenkinsConfiguredForProjectBootstrap();
    }

    /*
     * ############################
     * Jenkins security related
     * ############################
     */
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
