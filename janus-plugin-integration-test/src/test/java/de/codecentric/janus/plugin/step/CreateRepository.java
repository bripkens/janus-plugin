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
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.Matchers.*;
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

    @When("the repository creation page is accessed")
    public void whenTheRepositoryCreationPageIsAccessed() throws Exception {
        goToNewRepositoryPage();
    }

    /*
    * ############################
    * ### THEN
    * ############################
    */
    @Then("the repository creation success page is shown")
    public void thenTheRepositoryCreationSuccessPageIsShown() {
        String exp = "Repository successfully created";
        assertThat(driver.getPageSource(), containsString(exp));
    }

    @Then("a message is shown, that no valid VCS configuration exists")
    public void thenAMessageIsShownThatNoValidVCSConfigurationExists() {
        String exp = "No Version Control System configured";
        assertThat(driver.getPageSource(), containsString(exp));
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    public WebElement getNameInputField() {
        return findByCSS(CSS_SELECTOR.NAME_INPUT_FIELD);
    }

    public Select getVCSSelectField() {
        return findSelectByCSS(CSS_SELECTOR.VCS_SELECT_BOX);
    }

    public WebElement getSubmitButton() {
        return findByCSS(CSS_SELECTOR.SUBMIT_BUTTON);
    }

    private static interface CSS_SELECTOR {
        String NAME_INPUT_FIELD = "#main-panel input[name=\"_.name\"]";
        
        String VCS_SELECT_BOX = "#main-panel select";

        String SUBMIT_BUTTON = "#main-panel button";
    }
}
