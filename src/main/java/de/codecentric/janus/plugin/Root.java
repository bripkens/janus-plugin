package de.codecentric.janus.plugin;

import de.codecentric.janus.scaffold.Catalog;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.RootAction;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import javax.management.DescriptorAccess;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class Root implements RootAction, Describable<Root>{
    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New project";
    }

    public String getUrlName() {
        return "new-project";
    }

    public DescriptorImpl getDescriptor() {
        return new DescriptorImpl();
    }

    public Catalog getCatalog() {
        return Catalog.from(new File(getDescriptor().getCatalogFile()));
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<Root> {
        private String scaffoldDirectory;
        private String catalogFile;

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Janus configuration";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData)
                throws FormException {
            scaffoldDirectory = formData.getString("scaffoldDirectory");
            catalogFile = formData.getString("catalogFile");
            save();
            return super.configure(req,formData);
        }

        public String getScaffoldDirectory() {
            return scaffoldDirectory;
        }

        public String getCatalogFile() {
            return catalogFile;
        }
    }
}
