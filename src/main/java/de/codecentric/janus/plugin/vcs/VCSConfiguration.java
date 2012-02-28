package de.codecentric.janus.plugin.vcs;

import de.codecentric.janus.VersionControlSystem;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class VCSConfiguration implements Describable<VCSConfiguration> {
    private String name;
    private VersionControlSystem vcs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VersionControlSystem getVcs() {
        return vcs;
    }

    public void setVcs(VersionControlSystem vcs) {
        this.vcs = vcs;
    }

    public ConfigurationView getDescriptor() {
        return new ConfigurationView();
    }

}
