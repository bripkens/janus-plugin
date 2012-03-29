package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.plugin.JanusPlugin;
import hudson.model.Action;
import hudson.model.Hudson;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import org.acegisecurity.AccessDeniedException;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class BootstrapResultAction implements Action, AccessControlled {
    public static String URL = "result";

    public static final Permission PERMISSION = JanusPlugin.BOOTSTRAP_PROJECT;

    public String getIconFileName() {
        return "gear.png";
    }

    public String getDisplayName() {
        return "Results";
    }

    public String getUrlName() {
        return URL;
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
