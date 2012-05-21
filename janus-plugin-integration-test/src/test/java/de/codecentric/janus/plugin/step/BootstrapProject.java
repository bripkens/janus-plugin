/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.AbstractStep;
import org.apache.commons.io.IOUtils;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
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
    @Given("a configured scaffold directory <scaffoldDir> and catalog <catalog> and working directory <tmpDir>")
    public void givenAConfiguredScaffoldDirectoryAndCatalog(@Named("scaffoldDir") String scaffoldDir,
                                                            @Named("catalog") String catalog,
                                                            @Named("tmpDir") String tmpDir)
            throws Exception {
        goToConfigurationPage();

        String absoluteCatalogPath = prefixCWD(catalog);
        String absoluteScaffoldsPath = prefixCWD(scaffoldDir);
        String absoluteTmpDirPath = prefixCWD(tmpDir);
        generationConfiguration.getCatalogFileInputField()
                .sendKeys(absoluteCatalogPath);
        generationConfiguration.getScaffoldDirInputField()
                .sendKeys(absoluteScaffoldsPath);
        generationConfiguration.getTmpDirInputField()
                .sendKeys(absoluteTmpDirPath);
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

    @When("a project $projectName with package $package and VCS $vcsName is bootstrapped")
    public void projectIsBootStrapped(@Named("name") String projectName,
                                      @Named("package") String pckg,
                                      @Named("vcsName") String vcsName)
            throws Exception {
        goToProjectBootstrapPage();

        getNameInputField().sendKeys(projectName);
        getPckgInputField().sendKeys(pckg);
        getVCSSelectField().selectByValue("vcs-" + vcsName);
        getScaffoldSelectField().selectByValue("scaf-" + TEST_DATA.WEB_NAME);

        getSubmitButton().click();
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
    
    @Then("the version control system $name with type $type can be selected")
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

    @Then("the bootstrap success message is shown")
    public void thenTheBootstrapSuccessMessageIsShown() {
        String exp = "Successfully bootstrapped project!";
        assertThat(driver.getPageSource(), containsString(exp));
    }

    @Then("no failure log message is shown")
    public void thenNoFailureLogMessageIsShown() {
        assertTrue(findAllByCSS(CSS_SELECTOR.FAILURE_MESSAGES).isEmpty());
    }

    @Then("the target directory contains the scaffold named $projectName")
    public void thenTheTargetDirectoryContains(@Named("projectName") String projectName) {
        File path = new File(getTestTmpDir() + File.separator + projectName);
        assertTrue(path.exists());
        assertTrue(path.isDirectory());
    }

    @Then("the pom.xml file contains the artifactId $projectName and groupId $groupId")
    public void thenThePomContains(@Named("projectName") String projectName,
                                   @Named("groupId") String groupId)
            throws Exception {
        String path;
        path = getTestTmpDir() + File.separator + projectName + File.separator +
                "pom.xml";

        String pom = IOUtils.toString(new FileInputStream(new File(path)));

        assertThat(pom, containsString(projectName));
        assertThat(pom, containsString(groupId));
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public WebElement getNameInputField() {
        return findByCSS(CSS_SELECTOR.NAME_INPUT_FIELD);
    }

    public WebElement getPckgInputField() {
        return findByCSS(CSS_SELECTOR.PACKAGE_INPUT_FIELD);
    }

    public Select getVCSSelectField() {
        return findAllSelectsByCSS(CSS_SELECTOR.VCS_SELECT_BOX).get(0);
    }

    public Select getCISelectField() {
        return findAllSelectsByCSS(CSS_SELECTOR.SCAFFOLD_SELECT_BOX).get(1);
    }

    public Select getScaffoldSelectField() {
        return findAllSelectsByCSS(CSS_SELECTOR.SCAFFOLD_SELECT_BOX).get(4);
    }

    public WebElement getScaffoldDescriptionDIV() {
        return findByCSS(CSS_SELECTOR.SCAFFOLD_DESCRIPTION_DIV);
    }

    public WebElement getSubmitButton() {
        return findByCSS(CSS_SELECTOR.SUBMIT_BUTTON);
    }

    private static interface CSS_SELECTOR {
        String NAME_INPUT_FIELD = "input[name=\"_.name\"]";

        String PACKAGE_INPUT_FIELD = "input[name=\"_.pckg\"]";
        
        String VCS_SELECT_BOX = "select";

        String SCAFFOLD_SELECT_BOX = "select";

        String SCAFFOLD_DESCRIPTION_DIV = ".scaffoldDescription";
        
        String SUBMIT_BUTTON = "#main-panel button";
        
        String FAILURE_MESSAGES = ".FAILURE";
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
