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
import org.jbehave.core.annotations.Given;
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
    @Given("the Janus generation is configured")
    public void whenTheJanusGenerationIsConfigured() throws Exception {
        goToConfigurationPage();

        getCatalogFileInputField().sendKeys(getTestCatalogFile());
        getScaffoldDirInputField().sendKeys(getTestScaffoldDir());
        getTmpDirInputField().sendKeys(getTestTmpDir());

        configuration.getSubmitButton().click();
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
