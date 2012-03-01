package de.codecentric.janus.plugin;

import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;
import jenkins.model.Messages;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JanusPlugin {

    public static final PermissionGroup PERMISSIONS = new PermissionGroup(
            JanusPlugin.class, null);

    Permission CREATE_REPOSITORY = new Permission(PERMISSIONS,
            "CREATE_REPOSITORY",
            null,
            Permission.CREATE,
            PermissionScope.COMPUTER);
}
