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

package de.codecentric.janus.plugin.bootstrap.step;

import com.atlassian.confluence.rpc.soap.beans.RemoteSpace;
import com.atlassian.jira.rpc.soap.beans.RemoteGroup;
import com.atlassian.jira.rpc.soap.beans.RemoteProject;
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRole;
import com.atlassian.jira.rpc.soap.beans.RemoteUser;
import de.codecentric.janus.atlassian.AtlassianException;
import de.codecentric.janus.atlassian.confluence.ConfluenceClient;
import de.codecentric.janus.atlassian.confluence.ConfluenceSession;
import de.codecentric.janus.atlassian.confluence.SpacePermission;
import de.codecentric.janus.atlassian.jira.JiraClient;
import de.codecentric.janus.atlassian.jira.JiraSession;
import de.codecentric.janus.plugin.bootstrap.BootstrapLogger;
import de.codecentric.janus.plugin.bootstrap.LogEntry.Type;
import de.codecentric.janus.plugin.bootstrap.ParsedFormData;
import de.codecentric.janus.plugin.bootstrap.UserFormData;
import de.codecentric.janus.plugin.jira.JiraConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JiraConfigurationStep extends AbstractBootstrapStep {

    private static final Logger LOGGER = Logger.getLogger(JiraConfigurationStep.class.getName());

    private static final Pattern LETTER_PATTERN = Pattern.compile("(?i)[^a-z]*");
    private static final SpacePermission[] DEFAULT_SPACE_PERMISSIONS = new SpacePermission[] {
            SpacePermission.VIEW,
            SpacePermission.CREATE_PAGE,
            SpacePermission.EXPORT_PAGE,
            SpacePermission.CREATE_NEWS,
            SpacePermission.COMMENT,
            SpacePermission.CREATE_ATTACHMENT
    };

    private final String projectKey;

    private JiraSession jiraSession;
    private JiraClient jiraClient;
    private ConfluenceSession confluenceSession;
    private ConfluenceClient confluenceClient;

    private List<RemoteUser> jiraUsers;
    private RemoteGroup jiraGroup;
    private RemoteProject jiraProject;
    private RemoteSpace confluenceSpace;

    public JiraConfigurationStep(ParsedFormData data, BootstrapLogger logger) {
        super(data, logger);
        jiraUsers = new LinkedList<RemoteUser>();

        projectKey = data.getJiraProjectKey();
    }

    @Override
    public boolean execute() {
        logger.log("Configuring Jira and Confluence");

        try {
            establishConnections();

            addUsers();
            addGroup();
            addUsersToGroup();
            addProjectToJira();
            addGroupToDeveloperRole();

            addSpaceToConfluence();
            removeAnonymousAccessRights();
            addGroupToSpace();
            authorizeProjectLeader();
        } catch (AtlassianException ex) {
            logger.log("Jira and Confluence configuration failed.",
                    Type.FAILURE);
            LOGGER.log(Level.WARNING,
                    "Jira and Confluence configuration failed.", ex);
            return false;
        } finally {
            closeConnections();
        }

        logger.log("Successfully configured Jira.", Type.SUCCESS);
        return true;
    }

    private void addUsers() {
        logger.log("Adding users to JIRA.");
        for (UserFormData data : this.data.getJiraUsers()) {
            if (data.isNewUser()) {
                RemoteUser user = jiraClient.createUser(data.getUsername(),
                        data.getPassword(),
                        data.getFullName(),
                        data.getEmail());

                jiraUsers.add(user);
            } else {
                jiraUsers.add(jiraClient.searchUser(data.getEmail())[0]);
            }
        }
    }

    private void addGroup() {
        String groupName = data.getJiraGroupName();
        logger.log("Adding group '" + groupName + "' to JIRA and Confluence.");

        // even though the user database is shared, we add the group already
        // to avoid synchronization problems.
        // Without this, it would not be possible to change Confluence
        // group permission schemes.
        // Confluence is smart enough to figure out that groups with the
        // same name are the same, i.e., not synchronization issues.
        confluenceClient.addGroup(groupName);

        try {
            jiraGroup = jiraClient.getGroup(groupName);
        } catch (AtlassianException ex) {
            jiraGroup = jiraClient.createGroup(groupName);
        }
    }

    private void addUsersToGroup() {
        logger.log("Adding users to group '" + jiraGroup.getName() +
                "' in JIRA.");

        for (RemoteUser user : jiraUsers) {
            jiraClient.addUserToGroup(jiraGroup, user);
        }
    }

    private void addProjectToJira() {
        String projectName = data.getProject().getName();
        logger.log("Creating project '" + projectName + "' with key '" +
                projectKey + "' in JIRA.");

        RemoteProject project = new RemoteProject();

        project.setKey(projectKey);
        project.setName(projectName);
        project.setDescription(data.getProject().getDescription());
        project.setPermissionScheme(jiraClient
                .getPermissionScheme(data.getJiraPermissionScheme()));
        project.setNotificationScheme(jiraClient
                .getNotificationScheme("Default Notification Scheme"));
        project.setLead(jiraUsers.get(0).getName());

        jiraProject = jiraClient.createProject(project);
    }

    private void addGroupToDeveloperRole() {
        logger.log("Adding group '" + jiraGroup.getName() +
                "' to project's developer role.");

        RemoteProjectRole role = jiraClient.getProjectRole("Developers");
        jiraClient.addGroupToRole(jiraProject, jiraGroup, role);
    }

    private void addSpaceToConfluence() {
        String spaceName = data.getProject().getName();
        logger.log("Adding Confluence space '" + spaceName + "' with key '" +
                projectKey + "'.");

        RemoteSpace space = new RemoteSpace();
        space.setKey(projectKey);
        space.setName(spaceName);
        space.setDescription(data.getProject().getDescription());
        confluenceSpace = confluenceClient.addSpace(space);
    }

    private void removeAnonymousAccessRights() {
        logger.log("Revoking all anonymous access rights for space '" +
                data.getProject().getName() + "'.");
        confluenceClient.removeAllAnonymousPermissionsFromSpace(confluenceSpace
                .getKey());
    }

    private void addGroupToSpace() {
        logger.log("Authorizing group '" + jiraGroup.getName() +
                "'.");

        String groupName = jiraGroup.getName(),
                key = confluenceSpace.getKey();
        for (SpacePermission permission : DEFAULT_SPACE_PERMISSIONS) {
            confluenceClient.addPermissionToSpace(permission, groupName, key);
        }
    }

    private void authorizeProjectLeader() {
        String leader = jiraUsers.get(0).getName(),
            spaceKey = confluenceSpace.getKey();
        logger.log("Authorizing project leader '" + leader + "'.");

        for (SpacePermission permission : SpacePermission.values()) {
            confluenceClient.addPermissionToSpace(permission, leader, spaceKey);
        }
    }

    private void establishConnections() {
        JiraConfiguration config = data.getJiraConfiguration();

        logger.log("Establishing Jira session");
        jiraSession = new JiraSession(config.getJiraUrl(),
                config.getUsername(),
                config.getPassword());
        jiraClient = new JiraClient(jiraSession);

        logger.log("Establishing Confluence session");
        confluenceSession = new ConfluenceSession(config.getConfluenceUrl(),
                config.getUsername(),
                config.getPassword());
        confluenceClient = new ConfluenceClient(confluenceSession);
    }

    private void closeConnections() {
        logger.log("Closing Jira and Confluence sessions");

        if (jiraSession != null) {
            jiraSession.close();
        }

        if (jiraSession != null) {
            confluenceSession.close();
        }
    }

    private void printDebug() {
        logger.log("#####################################");
        logger.log("Jira config: " + data.getJiraConfiguration().getName());
        logger.log("Permission scheme: " + data.getJiraPermissionScheme());
        logger.log("Group: " + data.getJiraGroupName());
        logger.log("Users: ");
        List<UserFormData> users = data.getJiraUsers();
        for (int i = 0; i < users.size(); i++) {
            UserFormData user = users.get(i);
            logger.log("    [" + i + "] " + user.getUsername() + " <" + user.getEmail() + ">");
        }
        logger.log("#####################################");
    }
}
