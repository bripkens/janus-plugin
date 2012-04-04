package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import org.jbehave.core.annotations.Pending;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebElement;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class GenerationConfiguration extends AbstractStep {

    private Configuration configuration;

    @Inject
    public GenerationConfiguration(SeleniumAdapter selenium,
                                   Configuration configuration) {
        super(selenium);
        this.configuration = configuration;
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    /*
     * ############################
     * ### WHEN
     * ############################
     */
    @When("the Janus generation is configured")
    public void whenTheJanusGenerationIsConfigured() throws Exception {
        goToConfigurationPage();

        getCatalogFileInputField().sendKeys(getTestCatalogFile());
        getScaffoldDirInputField().sendKeys(getTestScaffoldDir());
        getTmpDirInputField().sendKeys(getTestTmpDir());

        configuration.getSubmitButton().click();
    }

    private String getTestScaffoldDir() {
        return System.getProperty("user.dir");
    }
    
    private String getTestCatalogFile() {
        return System.getProperty("user.dir") + File.separator + "pom.xml";
    }
    
    private String getTestTmpDir() {
        return System.getProperty("user.dir") + File.separator + "target";
    }
    
    /*
     * ############################
     * ### THEN
     * ############################
     */
    @Then("the Janus generation configuration is persisted")
    public void thenTheJanusGenerationConfigurationIsPersisted()
            throws Exception {
        goToConfigurationPage();

        assertThat(getCatalogFileInputField().getAttribute("value"),
                is(equalTo(getTestCatalogFile())));

        assertThat(getScaffoldDirInputField().getAttribute("value"),
                is(equalTo(getTestScaffoldDir())));

        assertThat(getTmpDirInputField().getAttribute("value"),
                is(equalTo(getTestTmpDir())));
    }

    
    
    /*
     * ############################
     * ### WEB ELEMENTS
     * ############################
     */
    public WebElement getCatalogFileInputField() {
        return findByCSS(CSS_SELECTOR.CATALOG_FILE_INPUT_FIELD);
    }

    public WebElement getScaffoldDirInputField() {
        return findByCSS(CSS_SELECTOR.SCAFFOLD_DIR_INPUT_FIELD);
    }

    public WebElement getTmpDirInputField() {
        return findByCSS(CSS_SELECTOR.TMP_DIR_INPUT_FIELD);
    }

    public static interface CSS_SELECTOR {
        String CATALOG_FILE_INPUT_FIELD = "input[name=\"_.catalogFile\"]";
        String SCAFFOLD_DIR_INPUT_FIELD = "input[name=\"_.scaffoldDirectory\"]";
        String TMP_DIR_INPUT_FIELD = "input[name=\"_.tempDirectory\"]";
    }
}
