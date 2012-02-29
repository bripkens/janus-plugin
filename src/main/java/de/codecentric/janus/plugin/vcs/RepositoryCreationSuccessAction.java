package de.codecentric.janus.plugin.vcs;

import hudson.model.Action;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCreationSuccessAction implements Action {
    public static final String URL = "success";
    
    public String getIconFileName() {
        return "gear.png";
    }

    public String getDisplayName() {
        return "Repository successfully created";
    }

    public String getUrlName() {
        return URL;
    }
}
