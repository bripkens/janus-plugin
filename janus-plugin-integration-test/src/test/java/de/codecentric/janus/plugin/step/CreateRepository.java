package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class CreateRepository {
    private WebDriver driver;
    private SeleniumAdapter seleniumAdapter;

    @Inject
    public CreateRepository(SeleniumAdapter selenium) {
        driver = selenium.getDriver();
        this.seleniumAdapter = selenium;
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
        seleniumAdapter.goToNewRepositoryPage();

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
        seleniumAdapter.goToLastSuccessfulBuild(build);

        WebElement status;
        status = driver.findElement(By.cssSelector("#main-panel h1 img"));

        assertThat(status.getAttribute("title"), is(equalTo("Success")));
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public WebElement getNameInputField() {
        return driver.findElement(By.cssSelector("#main-panel " +
                "input[name=\"_.name\"]"));
    }

    public Select getVCSSelectField() {
        By selector = By.cssSelector("#main-panel select");
        return new Select(driver.findElement(selector));
    }

    public WebElement getSubmitButton() {
        return driver.findElement(By.cssSelector("#main-panel button"));
    }
}
