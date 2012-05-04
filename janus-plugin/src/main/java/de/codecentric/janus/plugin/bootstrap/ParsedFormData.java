package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.conf.Project;
import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.jira.JiraConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.CatalogEntry;

import java.util.List;
import java.util.Map;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ParsedFormData {

    enum Status {
        OK,ERROR
    }
    
    private Status status;

    private Project project;
    private String jiraGroupName, jiraPermissionScheme;
    private CatalogEntry scaffold;
    private VCSConfiguration vcsConfiguration;
    private CIConfiguration ciConfiguration;
    private JiraConfiguration jiraConfiguration;
    private Map<String, String> context;
    private List<UserFormData> jiraUsers;

    public ParsedFormData(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getJiraGroupName() {
        return jiraGroupName;
    }

    public void setJiraGroupName(String jiraGroupName) {
        this.jiraGroupName = jiraGroupName;
    }

    public String getJiraPermissionScheme() {
        return jiraPermissionScheme;
    }

    public void setJiraPermissionScheme(String jiraPermissionScheme) {
        this.jiraPermissionScheme = jiraPermissionScheme;
    }

    public CatalogEntry getScaffold() {
        return scaffold;
    }

    public void setScaffold(CatalogEntry scaffold) {
        this.scaffold = scaffold;
    }

    public VCSConfiguration getVcsConfiguration() {
        return vcsConfiguration;
    }

    public void setVcsConfiguration(VCSConfiguration vcsConfiguration) {
        this.vcsConfiguration = vcsConfiguration;
    }

    public CIConfiguration getCiConfiguration() {
        return ciConfiguration;
    }

    public void setCiConfiguration(CIConfiguration ciConfiguration) {
        this.ciConfiguration = ciConfiguration;
    }

    public JiraConfiguration getJiraConfiguration() {
        return jiraConfiguration;
    }

    public void setJiraConfiguration(JiraConfiguration jiraConfiguration) {
        this.jiraConfiguration = jiraConfiguration;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public List<UserFormData> getJiraUsers() {
        return jiraUsers;
    }

    public void setJiraUsers(List<UserFormData> jiraUsers) {
        this.jiraUsers = jiraUsers;
    }

    @Override
    public String toString() {
        return "ParsedFormData{" +
                "status=" + status +
                ", project=" + project +
                ", jiraGroupName='" + jiraGroupName + '\'' +
                ", jiraPermissionScheme='" + jiraPermissionScheme + '\'' +
                ", scaffold=" + scaffold +
                ", vcsConfiguration=" + vcsConfiguration +
                ", ciConfiguration=" + ciConfiguration +
                ", jiraConfiguration=" + jiraConfiguration +
                ", context=" + context +
                ", jiraUsers=" + jiraUsers +
                '}';
    }
}
