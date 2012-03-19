package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Configuration {
    private WebDriver driver;
    private SeleniumAdapter seleniumAdapter;

    @Inject
    public Configuration(SeleniumAdapter selenium) {
        driver = selenium.getDriver();
        this.seleniumAdapter = selenium;
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    @Given("a clean Jenkins installation")
    public void givenACleanJenkinsInstallation() throws Exception {
        seleniumAdapter.cleanJenkinsConfiguration();
    }

    /*
     * ############################
     * ### WHEN
     * ############################
     */
    @When("a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void whenAVCSConfigurationIsAdded(@Named("name") String name,
                                             @Named("type") String type,
                                             @Named("creationBuild") String creationBuild,
                                             @Named("checkoutBuild") String checkoutBuild,
                                             @Named("commitBuild") String commitBuild)
            throws Exception {
        seleniumAdapter.goToConfigurationPage();

        // add a new entry to the list
        driver.findElement(By
                .cssSelector(".janusConfig + div .repeatable-add button"))
                .click();

        // wait until a new configuration entry is added the repeatable list
        String repeatedChunkSelector = ".janusConfig + div .repeated-chunk";
        seleniumAdapter.waitUntilPageContains(By
                .cssSelector(repeatedChunkSelector));

        // set values
        driver.findElement(By.cssSelector(repeatedChunkSelector + " input[name=\"_.name\"]")).sendKeys(name);
        select(By.cssSelector(repeatedChunkSelector + " select[name=\"_.vcs\"]"), type);
        select(By.cssSelector(repeatedChunkSelector + " select[name=\"_.generationBuildJob\"]"), creationBuild);
        select(By.cssSelector(repeatedChunkSelector + " select[name=\"_.checkoutBuildJob\"]"), checkoutBuild);
        select(By.cssSelector(repeatedChunkSelector + " select[name=\"_.commitBuildJob\"]"), commitBuild);

        // submit the form
        driver.findElement(By.cssSelector("#bottom-sticker button")).click();
    }
    
    private void select(By by, String selectValue) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(selectValue);
    }

    /*
     * ############################
     * ### THEN
     * ############################
     */
    @Then("a <type> installation <name> can be found with builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void thenAnCanBeFound(@Named("type") String type,
                                 @Named("name") String name,
                                 @Named("creationBuild") String creationBuild,
                                 @Named("checkoutBuild") String checkoutBuild,
                                 @Named("commitBuild") String commitBuild) throws Exception {
        seleniumAdapter.goToConfigurationPage();

        String repeatedChunkSelector = ".janusConfig + div .repeated-chunk";

        By nameSelector = By.cssSelector(repeatedChunkSelector +
                " input[name=\"_.name\"]");
        assertThat(driver.findElement(nameSelector).getAttribute("value"),
                is(equalTo(name)));
        
        assertSelected(By.cssSelector(repeatedChunkSelector +
                " select[name=\"_.vcs\"]"), type);
        assertSelected(By.cssSelector(repeatedChunkSelector +
                " select[name=\"_.generationBuildJob\"]"), creationBuild);
        assertSelected(By.cssSelector(repeatedChunkSelector +
                " select[name=\"_.checkoutBuildJob\"]"), checkoutBuild);
        assertSelected(By.cssSelector(repeatedChunkSelector +
                " select[name=\"_.commitBuildJob\"]"), commitBuild);

        
    }
    
    private void assertSelected(By by, String value) {
        Select select = new Select(driver.findElement(by));
        assertThat(select.getFirstSelectedOption().getAttribute("value"),
                is(equalTo(value)));
    }
}
