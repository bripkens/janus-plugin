package de.codecentric.janus.plugin;

import de.codecentric.janus.scaffold.Catalog;
import de.codecentric.janus.scaffold.CatalogEntry;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.RootAction;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.management.DescriptorAccess;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class Root implements RootAction, Describable<Root> {
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

    public void doSubmit(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        JSONObject submittedForm = req.getSubmittedForm();

        // the scaffold dropdown has no name
        CatalogEntry entry = getCatalogEntry(submittedForm);

        System.out.println(req.getSubmittedForm().toString());
        System.out.println("Form was submitted!");
        System.out.println(req.getSubmittedForm().get(""));
    }

    private CatalogEntry getCatalogEntry(JSONObject submittedForm) {
        int scaffoldIndex;
        try {
            scaffoldIndex = submittedForm.getInt("");
        } catch (JSONException ex) {
            throw new JanusGenerationException("Invalid scaffold index.");
        }
        CatalogEntry entry = getCatalog().getScaffolds().get(scaffoldIndex);

        if (entry == null) {
            throw new JanusGenerationException("Invalid scaffold index.");
        }

        return entry;
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

        public ListBoxModel doFillBuildJobItems() {
            ListBoxModel m = new ListBoxModel();
            m.add("Please select", "");

            Collection<String> allJobs = Hudson.getInstance().getJobNames();
            for(String job : allJobs) {
                m.add(job);
            }

            return m;
        }
    }
}
