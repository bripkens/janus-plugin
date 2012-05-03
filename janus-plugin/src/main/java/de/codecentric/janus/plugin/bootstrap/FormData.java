package de.codecentric.janus.plugin.bootstrap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
* @author Ben Ripkens <bripkens.dev@gmail.com>
*/
class FormData {
    private String name, description, pckg, scaffoldName, vcsConfigName,
            ciConfigName, jiraGroupName, jiraConfigName, jiraPermissionScheme;
    private Map<String, String> contextParameters;

    FormData() {
        contextParameters = new HashMap<String, String>();
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
        // {"":["vcs-Training","ci-Training","jira-Prototyping","scaf-Java EE 6 RESTful web service"],"description":"","jira":{"":["permissionScheme-Default Permission Scheme","","","","",""],"group":"jira-administrators"},"name":"","pckg":""}
        result.jiraGroupName = formData.getString("group");

        Iterator it = formData.getJSONArray("").iterator();
        while (it.hasNext()) {
            String item = (String) it.next();
            if (item.startsWith(PREFIX.PERMISSION_SCHEME)) {
                result.jiraPermissionScheme = item
                        .substring(PREFIX.PERMISSION_SCHEME.length());
            }
        }
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

        for(Map.Entry<String, String> entry : contextParameters.entrySet()) {
            req.setAttribute("param-" + entry.getKey(), entry.getValue());
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

    private interface PREFIX {
        String PERMISSION_SCHEME = "permissionScheme-";
        String SCAFFOLD = "scaf-";
        String CI = "ci-";
        String JIRA = "jira-";
        String VCS = "vcs-";
    }
}
