package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.suite.Config;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JiraConfiguration extends AbstractStep {
    private Configuration configuration;

    @Inject
    public JiraConfiguration(SeleniumAdapter selenium,
                           Configuration configuration) {
        super(selenium);
        this.configuration = configuration;
    }

    /*
    * ############################
    * ### GIVEN
    * ############################
    */
    @Given("a JIRA configuration $jiraName is added with the default integration test configuration")
    @When("the JIRA system is configured with name $jiraName")
    public void givenAStandardConfig(@Named("$jiraName") String name)
            throws Exception {
        goToConfigurationPage();

        // add a new entry to the list
        getAddJiraConfigButton().click();

        // wait until a new configuration entry is added the repeatable list
        waitUntilPageContains(By
                .cssSelector(CSS_SELECTOR.REPEATED_CHUNK_SELECTOR));

        getNameInputField().sendKeys(name);
        getUsernameInputField().sendKeys(Config.getDefaultJiraUsername());
        getPasswordInputField().sendKeys(Config.getDefaultJiraPassword());
        getJiraUrlInputField().sendKeys(Config.getDefaultJiraUrl());
        getConfluenceInputField().sendKeys(Config.getDefaultConfluenceUrl());

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
    private WebElement getAddJiraConfigButton() {
        return findByCSS(CSS_SELECTOR.ADD_JIRA_CONFIG_BUTTON);
    }

    private WebElement getNameInputField() {
        return findByCSS(CSS_SELECTOR.NAME_INPUT_FIELD);
    }

    private WebElement getJiraUrlInputField() {
        return findByCSS(CSS_SELECTOR.JIRA_URL_INPUT_FIELD);
    }

    private WebElement getConfluenceInputField() {
        return findByCSS(CSS_SELECTOR.CONFLUENCE_URL_INPUT_FIELD);
    }

    private WebElement getUsernameInputField() {
        return findByCSS(CSS_SELECTOR.USERNAME_INPUT_FIELD);
    }

    private WebElement getPasswordInputField() {
        return findByCSS(CSS_SELECTOR.PASSWORD_INPUT_FIELD);
    }

    public static interface CSS_SELECTOR {
        String ADD_JIRA_CONFIG_BUTTON = ".janusJiraConfig + div .repeatable-add " +
                "button";

        String REPEATED_CHUNK_SELECTOR = ".janusJiraConfig + div .repeated-chunk";

        String NAME_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.name\"]";

        String JIRA_URL_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.jiraUrl\"]";

        String CONFLUENCE_URL_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.confluenceUrl\"]";

        String USERNAME_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.username\"]";

        String PASSWORD_INPUT_FIELD = REPEATED_CHUNK_SELECTOR +
                " input[name=\"_.password\"]";
    }
}
