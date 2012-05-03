package de.codecentric.janus.plugin.bootstrap;

import com.atlassian.confluence.rpc.soap.beans.RemoteUserInformation;
import com.atlassian.jira.rpc.soap.beans.RemoteGroup;
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme;
import com.atlassian.jira.rpc.soap.beans.RemoteUser;
import de.codecentric.janus.atlassian.jira.JiraClient;
import de.codecentric.janus.atlassian.jira.JiraSession;
import de.codecentric.janus.atlassian.model.RemoteGroupSummary;
import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.Flash;
import de.codecentric.janus.plugin.FlashKeys;
import de.codecentric.janus.plugin.JanusPlugin;
import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.jira.JiraConfiguration;
import de.codecentric.janus.plugin.vcs.RepositoryCreationSuccessAction;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.Catalog;
import de.codecentric.janus.scaffold.CatalogEntry;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Hudson;
import hudson.model.RootAction;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import org.acegisecurity.AccessDeniedException;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
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
        System.out.println(req.getSubmittedForm());

        FormData formData = FormData.parse(req.getSubmittedForm());
        ParsedFormData parsedFormData = isValid(formData);
        if (parsedFormData.getStatus() == ParsedFormData.Status.OK) {
//            Project project = new Project();
//            project.setName(parsedFormData.getName());
//            project.setDescription(parsedFormData.getDescription());
//            project.setPckg(parsedFormData.getPckg());
//
//            BootstrapExecutor executor;
//            executor = new BootstrapExecutor(project,
//                    parsedFormData.getVcsConfiguration(),
//                    parsedFormData.getCiConfiguration(),
//                    parsedFormData.getScaffold(),
//                    parsedFormData.getContext());
//            Log log = executor.execute();
//
//            Flash flash = Flash.getForRequest(req);
//            flash.put(FlashKeys.BOOTSTRAP_LOG, log);
//
//            rsp.sendRedirect("/" + URL + "/" + BootstrapResultAction.URL);
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

    @JavaScriptMethod
    public RemoteGroupSummary[] getExistingGroups(String jiraConfigName,
                                                  String query) {
        JiraSession session = getJiraSession(getJiraConfig(jiraConfigName));
        JiraClient client = new JiraClient(session);

        RemoteGroupSummary[] groups;
        groups = client.searchGroups(query);

        session.close();
        return groups;
    }

    @JavaScriptMethod
    public RemoteUser[] getExistingUsers(String jiraConfigName,
                                                  String query) {
        JiraSession session = getJiraSession(getJiraConfig(jiraConfigName));
        JiraClient client = new JiraClient(session);

        RemoteUser[] users = client.searchUser(query);

        session.close();
        return users;
    }

    @JavaScriptMethod
    public String[] validateUserInput(String jiraConfigName,
                                      String username, String fullName,
                                      String email, String password) {
        JiraSession session = getJiraSession(getJiraConfig(jiraConfigName));
        JiraClient client = new JiraClient(session);

        String[] result = new String[] {
                validateNewUsername(client, username),
                validateNewFullName(client, fullName),
                validateNewEmail(client, email),
                validateNewPassword(client, password)
        };

        session.close();

        return result;
    }

    public String validateNewUsername(JiraClient client, String username) {
        if (GenericValidator.isBlankOrNull(username)) {
            return "Please enter a username.";
        } else if (isUsernameRegistered(client, username)) {
            return "This username is already used.";
        }

        return null;
    }

    private boolean isUsernameRegistered(JiraClient client, String username) {
        for (RemoteUser user : client.searchUser(username)) {
            if (user.getName().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public String validateNewFullName(JiraClient client, String fullName) {
        if (GenericValidator.isBlankOrNull(fullName)) {
            return "Please enter a full name.";
        }

        return null;
    }

    public String validateNewEmail(JiraClient client, String email) {
        if (GenericValidator.isBlankOrNull(email)) {
            return "Please enter an email address.";
        } else if (!EmailValidator.getInstance().isValid(email)) {
            return "Please enter a valid email address.";
        } else if (isEmailAddressRegistered(client, email)) {
            return "Email address already registered.";
        }

        return null;
    }

    private boolean isEmailAddressRegistered(JiraClient client, String email) {
        for (RemoteUser user : client.searchUser(email)) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public String validateNewPassword(JiraClient client, String password) {
        if (GenericValidator.isBlankOrNull(password)) {
            return "Please enter a password.";
        } else if (password.length() < 5) {
            return "Minimum password length is 5 characters.";
        }

        return null;
    }


    private JiraConfiguration getJiraConfig(String name) {
        for (JiraConfiguration config : getValidJiraConfigs()) {
            if (config.getName().equals(name)) {
                return config;
            }
        }

        throw new IllegalArgumentException("No such config was found.");
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
        
        VCSConfiguration vcsConfig;
        try {
            vcsConfig = getVCSConfig(formData);
        } catch (IllegalArgumentException ex) {
            return result;
        }
        result.setVcsConfiguration(vcsConfig);

        CIConfiguration ciConfig;
        try {
            ciConfig = getCIConfig(formData);
        } catch (IllegalArgumentException ex) {
            return result;
        }
        result.setCiConfiguration(ciConfig);

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

    private CIConfiguration getCIConfig(FormData formData) {
        for (CIConfiguration config : getValidCIConfigs()) {
            if (config.getName().equals(formData.getCiConfigName())) {
                return config;
            }
        }

        throw new IllegalArgumentException("The CI config doesn't exist");
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

    private JiraSession getJiraSession(JiraConfiguration config) {
        return new JiraSession(config.getJiraUrl(),
                config.getUsername(),
                config.getPassword());
    }

    /*
     * ############################
     * Getter for view
     * ############################
     */
    public VCSConfiguration[] getValidVCSConfigs() {
        return JanusPlugin.getValidVCSConfigs();
    }

    public CIConfiguration[] getValidCIConfigs() {
        return JanusPlugin.getValidCIConfigs();
    }

    public JiraConfiguration[] getValidJiraConfigs() {
        return JanusPlugin.getValidJiraConfigurations();
    }

    public Catalog getCatalog() {
        return JanusPlugin.getCatalog();
    }

    public boolean isJenkinsConfigured() {
        return JanusPlugin.isJenkinsConfiguredForProjectBootstrap();
    }

    public RemotePermissionScheme[] getPermissionSchemes(JiraConfiguration config) {
        JiraSession session = getJiraSession(config);
        JiraClient client = new JiraClient(session);

        RemotePermissionScheme[] schemes;
        schemes = client.getPermissionSchemes();

        session.close();
        return schemes;
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

    /*
     * ############################
     * Sub view navigation
     * ############################
     */
    public Action getDynamic(String name) {
        if (name.equals(BootstrapResultAction.URL)) {
            return new BootstrapResultAction();
        }

        return null;
    }
}
