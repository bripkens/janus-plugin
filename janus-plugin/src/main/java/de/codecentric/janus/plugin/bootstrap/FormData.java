package de.codecentric.janus.plugin.bootstrap;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @author Ben Ripkens <bripkens.dev@gmail.com>
*/
class FormData {
    private static final Logger LOGGER = Logger
            .getLogger(FormData.class.getName());

    private String name, description, pckg, scaffoldName, vcsConfigName,
            ciConfigName, jiraGroupName, jiraConfigName, jiraPermissionScheme;
    private Map<String, String> contextParameters;
    private List<UserFormData> users;

    FormData() {
        contextParameters = new HashMap<String, String>();
        users = new LinkedList<UserFormData>();
    }

    static FormData parse(JSONObject form) {
        FormData result = new FormData();

        result.name = form.getString("name");
        result.description = form.getString("description");
        result.pckg = form.getString("pckg");

        parseSelectValues(form.getJSONArray(""), result);
        parseContextParameters(form.getJSONObject("scaffold"), result);
        parseJiraParameters(form.getJSONObject("jira"), result);

        return result;
    }

    private static void parseSelectValues(JSONArray formData,
                                          FormData result) {
        for(int i = 0; i < formData.size(); i++) {
            String data = formData.getString(i);

            if (data != null) {
                if (data.startsWith(PREFIX.VCS)) {
                    result.vcsConfigName = data.substring(PREFIX.VCS.length());
                } else if (data.startsWith(PREFIX.SCAFFOLD)) {
                    result.scaffoldName = data
                            .substring(PREFIX.SCAFFOLD.length());
                } else if (data.startsWith(PREFIX.CI)) {
                    result.ciConfigName = data.substring(PREFIX.CI.length());
                } else if (data.startsWith(PREFIX.JIRA)) {
                    result.jiraConfigName = data
                            .substring(PREFIX.JIRA.length());
                }
            }
        }
    }

    private static void parseContextParameters(JSONObject formData,
                                               FormData result) {
        for(Object key : formData.keySet()) {
            String keyString = (String) key;
            keyString = keyString.trim();

            if (!keyString.isEmpty()) {
                String value = formData.getString(keyString);
                value = value.trim();
                if (!value.isEmpty()) {
                    result.contextParameters.put(keyString, value);
                }
            }
        }
    }

    private static void parseJiraParameters(JSONObject formData, FormData result) {
        result.jiraGroupName = formData.getString("group");

        Iterator it = formData.getJSONArray("").iterator();
        while (it.hasNext()) {
            String item = (String) it.next();
            if (item.startsWith(PREFIX.PERMISSION_SCHEME)) {
                result.jiraPermissionScheme = item
                        .substring(PREFIX.PERMISSION_SCHEME.length());
            }
        }

        if (formData.containsKey("userUsername")) {
            try {
                // to differentiate between a single user and multiple ones,
                // we can only try to convert it to an array
                formData.getJSONArray("userUsername");
                parseJiraUsers(formData, result);
            } catch (JSONException ex) {
                parseSingleJiraUser(formData, result);
            }
        }
    }

    private static void parseJiraUsers(JSONObject formData, FormData result) {
        JSONArray usernames = formData.getJSONArray("userUsername"),
                emails = formData.getJSONArray("userEmail"),
                fullNames = formData.getJSONArray("userFullName"),
                passwords = formData.getJSONArray("userPassword"),
                isNew = formData.getJSONArray("userNew");

        for (int i = 0; i < usernames.size(); i++) {
            UserFormData user = new UserFormData();

            user.setUsername(usernames.getString(i));
            user.setFullName(fullNames.getString(i));
            user.setPassword(passwords.getString(i));
            user.setEmail(emails.getString(i));
            user.setNewUser(isNew.getBoolean(i));

            result.users.add(user);
        }
    }

    private static void parseSingleJiraUser(JSONObject formData, FormData result) {
        UserFormData user = new UserFormData();

        user.setUsername(formData.getString("userEmail"));
        user.setFullName(formData.getString("userFullName"));
        user.setPassword(formData.getString("userPassword"));
        user.setEmail(formData.getString("userEmail"));
        user.setNewUser(formData.getBoolean("userNew"));

        result.users.add(user);
    }

    void setFormDataAsAttributesOn(StaplerRequest req) {
        req.setAttribute("name", name);
        req.setAttribute("description", description);
        req.setAttribute("pckg", pckg);
        req.setAttribute("group", jiraGroupName);
        req.setAttribute("selectedVCS", vcsConfigName);
        req.setAttribute("selectedCI", ciConfigName);
        req.setAttribute("selectedScaffold", scaffoldName);
        req.setAttribute("selectedJira", jiraConfigName);
        req.setAttribute("selectedPermissionScheme", jiraPermissionScheme);

        for (Map.Entry<String, String> entry : contextParameters.entrySet()) {
            req.setAttribute("param-" + entry.getKey(), entry.getValue());
        }

        try {
            req.setAttribute("addedUsers", new ObjectMapper().writeValueAsString(users));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to serialize added users.", e);
            // Ignoring the exception to allow the user to retain most of its
            // inputs.
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPckg() {
        return pckg;
    }

    public String getScaffoldName() {
        return scaffoldName;
    }

    public String getVcsConfigName() {
        return vcsConfigName;
    }

    public String getCiConfigName() {
        return ciConfigName;
    }

    public Map<String, String> getContextParameters() {
        return contextParameters;
    }

    public String getJiraGroupName() {
        return jiraGroupName;
    }

    public String getJiraConfigName() {
        return jiraConfigName;
    }

    public String getJiraPermissionScheme() {
        return jiraPermissionScheme;
    }

    public List<UserFormData> getUsers() {
        return users;
    }

    private interface PREFIX {
        String PERMISSION_SCHEME = "permissionScheme-";
        String SCAFFOLD = "scaf-";
        String CI = "ci-";
        String JIRA = "jira-";
        String VCS = "vcs-";
    }
}
