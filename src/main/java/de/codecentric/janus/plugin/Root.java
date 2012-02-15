package de.codecentric.janus.plugin;

import hudson.Extension;
import hudson.model.RootAction;

import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class Root implements RootAction{
    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New project";
    }

    public String getUrlName() {
        return "new-project";
    }
}
