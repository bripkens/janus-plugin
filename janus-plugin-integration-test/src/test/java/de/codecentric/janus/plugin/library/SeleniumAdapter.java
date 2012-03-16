package de.codecentric.janus.plugin.library;

import de.codecentric.janus.plugin.suite.Config;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.ScenarioType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SeleniumAdapter {
    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeStories
    public void initSelenium() throws MalformedURLException {
        if (Config.isLocalSeleniumExecution()) {
            driver = new FirefoxDriver();
        } else {
            throw new UnsupportedOperationException("Only local " +
                    "execution is possible.");
        }
    }

    @AfterStories
    public void afterStories() {
        driver.quit();
    }

    public void waitUntilPageContainsId(final By by) throws
            InterruptedException {
        new WebDriverWait(driver, 10)
                .until(new ExpectedCondition<WebElement>(){
                    @Override
                    public WebElement apply(WebDriver d) {
                        return d.findElement(by);
                    }});
    }
}
