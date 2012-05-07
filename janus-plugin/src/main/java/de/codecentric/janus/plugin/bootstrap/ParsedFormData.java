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
    private String jiraGroupName, jiraPermissionScheme, jiraProjectKey;
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

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    @Override
    public String toString() {
        return "ParsedFormData{" +
                "status=" + status +
                ", project=" + project +
                ", jiraGroupName='" + jiraGroupName + '\'' +
                ", jiraPermissionScheme='" + jiraPermissionScheme + '\'' +
                ", jiraProjectKey='" + jiraProjectKey + '\'' +
                ", scaffold=" + scaffold +
                ", vcsConfiguration=" + vcsConfiguration +
                ", ciConfiguration=" + ciConfiguration +
                ", jiraConfiguration=" + jiraConfiguration +
                ", context=" + context +
                ", jiraUsers=" + jiraUsers +
                '}';
    }
}
