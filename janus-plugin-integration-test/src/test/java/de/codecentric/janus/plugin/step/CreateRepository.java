package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CreateRepository extends AbstractStep {

    @Inject
    public CreateRepository(SeleniumAdapter selenium) {
        super(selenium);
    }

    /*
    * ############################
    * ### WHEN
    * ############################
    */
    @When("a repository <repositoryName> is created for vcs <name>")
    public void whenARepositoryOfTypeIsCreated(
            @Named("repositoryName") String repositoryName,
            @Named("name") String vcsName)
            throws Exception {
        goToNewRepositoryPage();

        getNameInputField().sendKeys(repositoryName);
        getVCSSelectField().selectByValue(vcsName);
        getSubmitButton().click();
    }

    /*
    * ############################
    * ### THEN
    * ############################
    */
    @Then("the repository creation success page is shown")
    public void thenTheRepositoryCreationSuccessPageIsShown() {
        String exp = "Repository successfully created";
        assertTrue(driver.getPageSource().contains(exp));
    }

    @Then("the build <creationBuild> is successfully executed")
    public void thenTheBuildIsSuccessfullyExecuted(
            @Named("creationBuild") String build) throws Exception {
        goToLastSuccessfulBuild(build);

        assertThat(getBuildStatusIndicator().getAttribute("title"),
                is(equalTo("Success")));
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public WebElement getNameInputField() {
        return findByCSS(CSS_SELECTORS.NAME_INPUT_FIELD);
    }

    public Select getVCSSelectField() {
        return findSelectByCSS(CSS_SELECTORS.VCS_SELECT_BOX);
    }

    public WebElement getSubmitButton() {
        return findByCSS(CSS_SELECTORS.SUBMIT_BUTTON);
    }

    public WebElement getBuildStatusIndicator() {
        return findByCSS(CSS_SELECTORS.BUILD_STATUS_INDICATOR);
    }

    private static interface CSS_SELECTORS {
        String BUILD_STATUS_INDICATOR = "#main-panel h1 img";
        
        String NAME_INPUT_FIELD = "#main-panel input[name=\"_.name\"]";
        
        String VCS_SELECT_BOX = "#main-panel select";

        String SUBMIT_BUTTON = "#main-panel button";
    }
}
