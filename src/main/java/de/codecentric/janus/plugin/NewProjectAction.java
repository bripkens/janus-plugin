package de.codecentric.janus.plugin;

import de.codecentric.janus.VersionControlSystem;
import de.codecentric.janus.conf.Project;
import de.codecentric.janus.conf.vcs.VCSConfig;
import de.codecentric.janus.scaffold.Catalog;
import de.codecentric.janus.scaffold.CatalogEntry;
import de.codecentric.janus.scaffold.Scaffold;
import de.codecentric.janus.scaffold.ScaffoldLoadingException;
import hudson.model.Describable;
import hudson.model.RootAction;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
//@Extension
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

        Project project = new Project();
        project.setName(submittedForm.getString("name"));
        project.setDescription(submittedForm.getString("description"));
        project.setPckg(submittedForm.getString("pckg"));

        Map<String, Object> context = req.getSubmittedForm()
                .getJSONObject("scaffold");

        Scaffold scaffold;
        try {
            scaffold = getScaffold(submittedForm);
        } catch (ScaffoldLoadingException ex) {
            // TODO show error message (scaffold couldn't be loaded)
            return;
        }

        VersionControlSystem vcs = getDescriptor().getVcs();
        VCSConfig vcsConfig = getVCSConfig(project, vcs);
    }
    
    private Scaffold getScaffold(JSONObject submittedForm) {
        CatalogEntry entry = getCatalogEntry(submittedForm);
        
        String filePath = getDescriptor().getScaffoldDirectory() +
                File.separator + entry.getFilename();
        return Scaffold.from(new File(filePath));
    }

    private CatalogEntry getCatalogEntry(JSONObject submittedForm) {
        int scaffoldIndex;
        try {
            scaffoldIndex = submittedForm.getInt("");
        } catch (JSONException ex) {
            throw new JanusPluginGenerationException("Invalid scaffold index.");
        }
        CatalogEntry entry = getCatalog().getScaffolds().get(scaffoldIndex);

        if (entry == null) {
            throw new JanusPluginGenerationException("Invalid scaffold index.");
        }

        return entry;
    }

    private VCSConfig getVCSConfig(Project project, VersionControlSystem vcs) {
        VCSConfig config = vcs.newConfig();
        config.setUrl(getDescriptor().getRepositoryURL()
                .replace("$NAME", project.getName()));
        return config;
    }
}
