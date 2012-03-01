package de.codecentric.janus.plugin;

import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JanusPlugin {

    public static final PermissionGroup PERMISSIONS = new PermissionGroup(
            JanusPlugin.class, Messages._permissions_group_title());

    Permission CREATE_REPOSITORY = new Permission(PERMISSIONS,
            "Create repository",
            null,
            Permission.CREATE,
            PermissionScope.COMPUTER);
}
