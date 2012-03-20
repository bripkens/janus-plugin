package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import org.jbehave.core.annotations.Given;
import org.openqa.selenium.WebElement;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Configuration extends AbstractStep {

    @Inject
    public Configuration(SeleniumAdapter selenium) {
        super(selenium);
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    @Given("a clean Jenkins installation")
    public void givenACleanJenkinsInstallation() throws Exception {
        cleanJenkinsConfiguration();
    }

    /*
     * ############################
     * ### WHEN
     * ############################
     */

    /*
     * ############################
     * ### THEN
     * ############################
     */

    /*
     * ############################
     * ### WEB ELEMENTS
     * ############################
     */
    public WebElement getSubmitButton() {
        return findByCSS(CSS_SELECTOR.SUBMIT_BUTTON);
    }


    public static interface CSS_SELECTOR {
        String SUBMIT_BUTTON = "#bottom-sticker button";
    }
}
