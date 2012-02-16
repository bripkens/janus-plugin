package de.codecentric.janus.plugin;

import de.codecentric.janus.scaffold.Catalog;
import de.codecentric.janus.scaffold.CatalogEntry;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.RootAction;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class NewProjectAction implements RootAction, Describable<NewProjectAction> {
    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New project";
    }

    public String getUrlName() {
        return "new-project";
    }

    public Configuration getDescriptor() {
        return new Configuration();
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
}
