package de.codecentric.janus.plugin.vcs;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.RootAction;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class NewRepositoryAction implements RootAction/*, Describable<NewRepositoryAction> */{
    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New repository";
    }

    public String getUrlName() {
        return "new-repository";
    }

//    public ConfigurationView getDescriptor() {
//        return new ConfigurationView();
//    }
}
