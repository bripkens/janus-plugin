package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Job {

    private WebDriver driver;
    private SeleniumAdapter seleniumAdapter;

    @Inject
    public Job(SeleniumAdapter selenium) {
        driver = selenium.getDriver();
        this.seleniumAdapter = selenium;
    }

    /*
    * ############################
    * ### GIVEN
    * ############################
    */
    @Given("a build <name>")
    public void givenABuild(@Named("name") String name) throws Exception {
        seleniumAdapter.goToBuildCreationPage();
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.cssSelector("input[value=\"hudson.model.FreeStyleProject\"]")).click();
        driver.findElement(By.id("ok-button")).click();
        seleniumAdapter.waitUntilPageTitleStartsWith(name + " Config");
    }

    @Given("a build <creationBuild>")
    public void givenACreationBuild(@Named("creationBuild") String name)
            throws Exception {
        givenABuild(name);
    }

    @Given("a build <checkoutBuild>")
    public void givenACheckoutBuild(@Named("checkoutBuild") String name)
            throws Exception {
        givenABuild(name);
    }

    @Given("a build <commitBuild>")
    public void givenACommitBuild(@Named("commitBuild") String name)
            throws Exception {
        givenABuild(name);
    }
}
