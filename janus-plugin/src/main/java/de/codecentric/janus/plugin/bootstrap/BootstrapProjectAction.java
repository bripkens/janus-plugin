package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import de.codecentric.janus.scaffold.CatalogEntry;
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
import java.util.List;
import java.util.Map;
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
        ParsedFormData parsedFormData = isValid(formData);
        if (parsedFormData.getStatus() == ParsedFormData.Status.OK) {
            Project project = new Project();
            project.setName(parsedFormData.getName());
            project.setDescription(parsedFormData.getDescription());
            project.setPckg(parsedFormData.getPckg());

            BootstrapExecutor executor;
            executor = new BootstrapExecutor(project,
                    parsedFormData.getVcsConfiguration(),
                    parsedFormData.getScaffold(), parsedFormData.getContext());
            List<String> log = executor.execute();
            
            for (String logEntry : log) {
                System.out.println(logEntry);
            }

            req.getRequestDispatcher("/").forward(req, rsp);
        } else {
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

    private ParsedFormData isValid(FormData formData) {
        ParsedFormData result;
        result = new ParsedFormData(ParsedFormData.Status.ERROR);

        if (!isValidName(formData.getName()) ||
                !isValidPckg(formData.getPckg()) ||
                !isValidDescription(formData.getDescription())) {
            return result;
        }
        result.setName(formData.getName());
        result.setDescription(formData.getDescription());
        result.setPckg(formData.getPckg());
        
        VCSConfiguration config;
        try {
            config = getVCSConfig(formData);
        } catch (IllegalArgumentException ex) {
            return result;
        }
        result.setVcsConfiguration(config);

        CatalogEntry scaffold;
        try {
            scaffold = getScaffold(formData);
        } catch (IllegalArgumentException ex) {
            return result;
        }
        result.setScaffold(scaffold);

        if (isRequiredContextFilledIn(scaffold, formData)) {
            result.setContext(formData.getContextParameters());
            result.setStatus(ParsedFormData.Status.OK);
        }

        return result;
    }

    private VCSConfiguration getVCSConfig(FormData formData) {
        for(VCSConfiguration config : getValidVCSConfigs()) {
            if (config.getName().equals(formData.getVcsConfigName())) {
                return config;
            }
        }

        throw new IllegalArgumentException("The VCS config doesn't exist");
    }
    
    private CatalogEntry getScaffold(FormData formData) {
        for (CatalogEntry entry : getCatalog().getScaffolds()) {
            if (entry.getName().equals(formData.getScaffoldName())) {
                return entry;
            }
        }

        throw new IllegalArgumentException("The scaffold doesn't exist");
    }
    
    private boolean isRequiredContextFilledIn(CatalogEntry scaffold,
                                              FormData formData) {
        Map<String, String> contextParams = formData.getContextParameters();

        for (String param : scaffold.getRequiredContext().keySet()) {
            if (!contextParams.containsKey(param)) {
                return false;
            }
        }

        return true;
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
