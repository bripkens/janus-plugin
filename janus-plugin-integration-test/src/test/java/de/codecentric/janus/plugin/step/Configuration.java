package de.codecentric.janus.plugin.step;

import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.Config;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.inject.Inject;
import org.openqa.selenium.support.ui.Select;

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
        
        Select vcsSelect = new Select(driver
                .findElement(By.cssSelector(repeatedChunkSelector + ":nth-child(1)")));

        vcsSelect.deselectAll();
        vcsSelect.selectByValue(type);
    }

    /*
     * ############################
     * ### THEN
     * ############################
     */
    @Then("a <type> installation <name> can be found")
    public void thenAnCanBeFound(@Named("type") String type,
                                 @Named("name") String name) {
    }
}
