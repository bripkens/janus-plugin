package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String absoluteCatalogPath = prefixCWD(catalog);
        String absoluteScaffoldsPath = prefixCWD(scaffoldDir);
        generationConfiguration.getCatalogFileInputField()
                .sendKeys(absoluteCatalogPath);
        generationConfiguration.getScaffoldDirInputField()
                .sendKeys(absoluteScaffoldsPath);
        configuration.getSubmitButton().click();
    }

    public String prefixCWD(String path) {
        if (path.startsWith("./")) {
            return System.getProperty("user.dir") + path.substring(1);
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
        List<WebElement> options = getVCSSelectField().getOptions();

        assertThat(options.size(), is(1));
        WebElement option = options.get(0);
        String value = option.getAttribute("value");
        String text = option.getText();
        assertThat(value, containsString(name));
        assertThat(text, containsString(type));
        assertThat(text, containsString(name));
    }

    @Then("the test project scaffolds are visible and can be selected")
    public void thenTheTestProjectScaffoldsAreVisible() throws Exception {
        List<WebElement> options = getScaffoldSelectField().getOptions();

        for (WebElement option : options) {
            String text = option.getText();
            if (text.equalsIgnoreCase(TEST_DATA.WEB_NAME)) {
                testWebScaffold(option);
            } else if (text.equalsIgnoreCase(TEST_DATA.DESKTOP_NAME)) {
                testDesktopScaffold(option);
            } else {
                fail("Unknown select option found.");
            }
        }
    }
    
    private void testWebScaffold(WebElement option) {
        option.click();
        testScaffoldWith(option, TEST_DATA.WEB_DESCRIPTION);
    }

    private void testDesktopScaffold(WebElement option) {
        testScaffoldWith(option, TEST_DATA.DESKTOP_DESCRIPTION,
                TEST_DATA.DESKTOP_REQUIRED_CONTEXT);
    }

    private void testScaffoldWith(WebElement option, String expDescription) {
        option.click();

        waitUntilPageContainsText(CSS_SELECTOR.SCAFFOLD_DESCRIPTION_DIV,
                expDescription);
    }

    private void testScaffoldWith(WebElement option, String expDescription, Map<String,
            String> expRequiredContext) {
        testScaffoldWith(option, expDescription);

        for(Map.Entry<String, String> entry : expRequiredContext.entrySet()) {
            String selector = "input[name=\"" + entry.getKey() + "\"]";
            WebElement input = findByCSS(selector);
            assertThat(input.isDisplayed(), is(true));
            
            WebElement errorMsg = findByCSS(selector + " + div.error");
            assertThat(errorMsg.isDisplayed(), is(true));
        }
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public Select getVCSSelectField() {
        return findAllSelectsByCSS(CSS_SELECTOR.VCS_SELECT_BOX).get(0);
    }

    public Select getScaffoldSelectField() {
        return findAllSelectsByCSS(CSS_SELECTOR.SCAFFOLD_SELECT_BOX).get(1);
    }

    public WebElement getScaffoldDescriptionDIV() {
        return findByCSS(CSS_SELECTOR.SCAFFOLD_DESCRIPTION_DIV);
    }

    private static interface CSS_SELECTOR {
        String VCS_SELECT_BOX = "select";

        String SCAFFOLD_SELECT_BOX = "select";

        String SCAFFOLD_DESCRIPTION_DIV = ".scaffoldDescription";
    }
    
    public static interface TEST_DATA {
        String WEB_NAME = "Java EE 6 RESTful web service";
        String WEB_DESCRIPTION = "Web based project with RESTeasy web service.";
        
        String DESKTOP_NAME = "Spring Desktop Application";
        String DESKTOP_DESCRIPTION = "Java Swing desktop application with Spring for DI.";
        Map<String, String> DESKTOP_REQUIRED_CONTEXT = new HashMap<String, String>() {{
            put("showSplash", "true/false. Whether to show the welcome screen when the application starts.");
            put("lookAndFeel", "Either 'CrossPlatformLookAndFeel' or 'SystemLookAndFeel'.");
        }};
    }
}
