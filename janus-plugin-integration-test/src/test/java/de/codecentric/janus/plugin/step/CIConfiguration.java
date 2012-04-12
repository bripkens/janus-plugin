package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.suite.Config;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CIConfiguration extends AbstractStep {
    private Configuration configuration;

    @Inject
    public CIConfiguration(SeleniumAdapter selenium,
                            Configuration configuration) {
        super(selenium);
        this.configuration = configuration;
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    @Given("a CI configuration $ciName is added with the default integration test user data")
    public void givenAStandardCiConfig(@Named("ciName") String name)
            throws Exception {
        goToConfigurationPage();

        // add a new entry to the list
        getAddCIConfigButton().click();

        // wait until a new configuration entry is added the repeatable list
        waitUntilPageContains(By
                .cssSelector(CSS_SELECTOR.REPEATED_CHUNK_SELECTOR));

        getNameInputField().sendKeys(name);
        getURLInputField().sendKeys("http://localhost:8080");
        getUsernameInputField().sendKeys(Config.getDefaultUsername());
        getApiTokenInputField().sendKeys(Config.getDefaultApiToken());

        // submit the form
        configuration.getSubmitButton().click();
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
    private WebElement getAddCIConfigButton() {
        return findByCSS(CSS_SELECTOR.ADD_VCS_CONFIG_BUTTON);
    }

    private WebElement getNameInputField() {
        return findByCSS(CSS_SELECTOR.NAME_INPUT_FIELD);
    }

    private WebElement getURLInputField() {
        return findByCSS(CSS_SELECTOR.URL_INPUT_FIELD);
    }

    private WebElement getUsernameInputField() {
        return findByCSS(CSS_SELECTOR.USERNAME_INPUT_FIELD);
    }

    private WebElement getApiTokenInputField() {
        return findByCSS(CSS_SELECTOR.API_TOKEN_INPUT_FIELD);
    }

    public static interface CSS_SELECTOR {
        String ADD_VCS_CONFIG_BUTTON = ".janusCIConfig + div .repeatable-add " +
                "button";

        String REPEATED_CHUNK_SELECTOR = ".janusCIConfig + div .repeated-chunk";

        String NAME_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.name\"]";

        String URL_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.url\"]";

        String USERNAME_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.username\"]";

        String API_TOKEN_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.apiToken\"]";
    }
}
