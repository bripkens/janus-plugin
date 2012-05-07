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
import org.openqa.selenium.WebElement;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Configuration extends AbstractStep {

    @Inject
    public Configuration(SeleniumAdapter selenium) {
        super(selenium);
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    @Given("a clean Jenkins installation")
    public void givenACleanJenkinsInstallation() throws Exception {
        cleanJenkinsConfiguration();
    }

    /*
     * ############################
     * ### WHEN
     * ############################
     */

    /*
     * ############################
     * ### THEN
     * ############################
     */

    /*
     * ############################
     * ### WEB ELEMENTS
     * ############################
     */
    public WebElement getSubmitButton() {
        return findByCSS(CSS_SELECTOR.SUBMIT_BUTTON);
    }


    public static interface CSS_SELECTOR {
        String SUBMIT_BUTTON = "#bottom-sticker button";
    }
}
