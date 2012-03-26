package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class BootstrapProject extends AbstractStep {

    private GenerationConfiguration generationConfiguration;
    private Configuration configuration;

    @Inject
    public BootstrapProject(SeleniumAdapter selenium,
                            GenerationConfiguration generationConfiguration,
                            Configuration configuration) {
        super(selenium);
        this.generationConfiguration = generationConfiguration;
        this.configuration = configuration;
    }

    /*
    * ############################
    * ### GIVEN
    * ############################
    */
    @Given("a configured scaffold directory <scaffoldDir> and catalog <catalog>")
    public void givenAConfiguredScaffoldDirectoryAndCatalog(@Named("scaffoldDir") String scaffoldDir,
                                                            @Named("catalog") String catalog)
            throws Exception {
        goToConfigurationPage();

        generationConfiguration.getCatalogFileInputField().sendKeys(catalog);
        generationConfiguration.getScaffoldDirInputField().sendKeys(scaffoldDir);
        configuration.getSubmitButton().click();
    }

    public String prefixCWD(String path) {
        if (path.startsWith("./")) {
            return System.getProperty("user.dir") + path.substring(2);
        }

        return path;
    }

    /*
    * ############################
    * ### WHEN
    * ############################
    */
    @When("the project bootstrap page is accessed")
    public void whenTheProjectBootstrapPageIsAccessed() throws Exception {
        goToProjectBootstrapPage();
    }

    /*
    * ############################
    * ### THEN
    * ############################
    */
    @Then("an invalid configuration error message is shown")
    public void thenAnInvalidConfigurationMessageIsShown() {
        assertThat(driver.getPageSource(),
                containsString("Jenkins is not properly configured"));
    }
    
    @Then("the version control system <name> with type <type> can be selected")
    public void thenVCSIsSelectable(@Named("name") String name,
                                    @Named("type") String type) {

    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public Select getVCSSelectField() {
        return findSelectByCSS(CSS_SELECTOR.VCS_SELECT_BOX);
    }

    public Select getScaffoldSelectField() {
        return findSelectByCSS(CSS_SELECTOR.SCAFFOLD_SELECT_BOX);
    }

    private static interface CSS_SELECTOR {
        String VCS_SELECT_BOX = "select:nth-of-type(1)";

        String SCAFFOLD_SELECT_BOX = "select:nth-of-type(2)";
    }
}
