package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.openqa.selenium.WebElement;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Job extends AbstractStep {

    @Inject
    public Job(SeleniumAdapter selenium) {
        super(selenium);
    }

    /*
    * ############################
    * ### GIVEN
    * ############################
    */
    @Given("a build $name")
    public void givenABuild(@Named("name") String name) throws Exception {
        goToBuildCreationPage();

        getNameInputField().sendKeys(name);
        getFreeStyleProjectCheckBox().click();
        getSubmitButton().click();

        waitUntilPageTitleStartsWith(name + " Config");
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    private WebElement getNameInputField() {
        return findById(ID_SELECTOR.NAME_INPUT_FIELD);
    }
    
    private WebElement getFreeStyleProjectCheckBox() {
        return findByCSS(CSS_SELECTOR.FREE_STYLE_PROJECT_RADIO_BUTTON);
    }

    private WebElement getSubmitButton() {
        return findById(ID_SELECTOR.SUBMIT_BUTTON);
    }
    
    private static interface CSS_SELECTOR {
        String FREE_STYLE_PROJECT_RADIO_BUTTON =
                "input[value=\"hudson.model.FreeStyleProject\"]";
    }

    private static interface ID_SELECTOR {
        String NAME_INPUT_FIELD = "name";

        String SUBMIT_BUTTON = "ok-button";
    }
}
