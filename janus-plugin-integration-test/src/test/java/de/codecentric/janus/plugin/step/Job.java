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
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Job extends AbstractStep {

    @Inject
    public Job(SeleniumAdapter selenium) {
        super(selenium);
    }

    /*
    * ############################
    * ### GIVEN
    * ############################
    */
    @Given("a build $name")
    public void givenABuild(@Named("name") String name) throws Exception {
        goToBuildCreationPage();

        getNameInputField().sendKeys(name);
        getFreeStyleProjectCheckBox().click();
        getSubmitButton().click();

        waitUntilPageTitleStartsWith(name + " Config");
    }

    /*
    * ############################
    * ### THEN
    * ############################
    */
    @Then("the build $build is successfully executed")
    public void thenTheBuildIsSuccessfullyExecuted(
            @Named("build") String build) throws Exception {
        goToLastSuccessfulBuild(build);

        assertThat(getBuildStatusIndicator().getAttribute("title"),
                is(equalTo("Success")));
    }

    @Then("the build $build is created")
    public void thenTheBuildsIsCreated(@Named("build") String name)
            throws Exception {
        goToBuild(name);

        // going to the build page is enough. In case the build doesn't exist,
        // the goToBuild(...) will already fail.
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    private WebElement getNameInputField() {
        return findById(ID_SELECTOR.NAME_INPUT_FIELD);
    }
    
    private WebElement getFreeStyleProjectCheckBox() {
        return findByCSS(CSS_SELECTOR.FREE_STYLE_PROJECT_RADIO_BUTTON);
    }

    private WebElement getSubmitButton() {
        return findById(ID_SELECTOR.SUBMIT_BUTTON);
    }
    
    public WebElement getBuildStatusIndicator() {
        return findByCSS(CSS_SELECTOR.BUILD_STATUS_INDICATOR);
    }

    private static interface CSS_SELECTOR {
        String FREE_STYLE_PROJECT_RADIO_BUTTON =
                "input[value=\"hudson.model.FreeStyleProject\"]";

        String BUILD_STATUS_INDICATOR = "#main-panel h1 img";
    }

    private static interface ID_SELECTOR {
        String NAME_INPUT_FIELD = "name";

        String SUBMIT_BUTTON = "ok-button";
    }
}
