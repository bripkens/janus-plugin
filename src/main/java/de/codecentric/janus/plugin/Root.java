package de.codecentric.janus.plugin;

import hudson.Extension;
import hudson.model.RootAction;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Extension
public class Root implements RootAction{
    List<String> scaffolds = Arrays.asList("Spring/Vadin web app",
            "Batch file processing", "Single page web app",
            "Transaction aware system");
    
    public String getIconFileName() {
        return "new-package.png";
    }

    public String getDisplayName() {
        return "New project";
    }

    public String getUrlName() {
        return "new-project";
    }
    
    public List<String> getScaffolds() {
        return scaffolds;
    }
}
