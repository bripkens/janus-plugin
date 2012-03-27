package de.codecentric.janus.plugin.suite;

import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractStep {
    protected WebDriver driver;
    protected SeleniumAdapter seleniumAdapter;

    public AbstractStep(SeleniumAdapter selenium) {
        driver = selenium.getDriver();
        this.seleniumAdapter = selenium;
    }

    public WebElement findByCSS(String cssSelector) {
        return driver.findElement(By.cssSelector(cssSelector));
    }
    
    public List<WebElement> findAllByCSS(String cssSelector) {
        return driver.findElements(By.cssSelector(cssSelector));
    }

    public WebElement findById(String id) {
        return driver.findElement(By.id(id));
    }

    public Select findSelectByCSS(String cssSelector) {
        return new Select(findByCSS(cssSelector));
    }
    
    public List<Select> findAllSelectsByCSS(String cssSelector) {
        List<Select> result = new ArrayList<Select>();

        for (WebElement element: findAllByCSS(cssSelector)) {
            result.add(new Select(element));
        }

        return result;
    }

    public void cleanJenkinsConfiguration() throws Exception {
        seleniumAdapter.cleanJenkinsConfiguration();
    }

    /*
    * ############################
    * ### NAVIGATION
    * ############################
    */
    public void goToConfigurationPage() throws Exception {
        driver.get(Config.getJenkinsBaseUrl() + "configure");
        waitUntilPageContains(By.className("janusConfig"));
    }

    public void goToBuildCreationPage() throws Exception {
        driver.get(Config.getJenkinsBaseUrl() + "view/All/newJob");
        waitUntilPageContains(By
                .cssSelector("form[name=\"createItem\"] #name"));
    }

    public void goToNewRepositoryPage()  throws Exception {
        driver.get(Config.getJenkinsBaseUrl() + "new-repository/");
        waitUntilPageTitleStartsWith("Janus: New project");
    }

    public void goToLastSuccessfulBuild(String job) throws Exception {
        driver.get(Config.getJenkinsBaseUrl() + "job/" + job + "/lastBuild/");
        // a timeout can indicate, that no build was executed (there is no
        // last build).
        waitUntilPageTitleStartsWith(job + " #");
    }

    public void goToProjectBootstrapPage() throws Exception {
        driver.get(Config.getJenkinsBaseUrl() + "bootstrap-project/");
        waitUntilPageTitleStartsWith("Janus: Bootstrap Project");
    }

    /*
    * ############################
    * ### WAITING
    * ############################
    */
    public void waitUntilPageContains(final By by) throws
            InterruptedException {
        waitUntil(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                return d.findElement(by);
            }
        });
    }

    public <T> void waitUntil(ExpectedCondition<T> condition) {
        new WebDriverWait(driver, Config.getTimeoutInSeconds())
                .until(condition);
    }

    public void waitUntilPageTitleStartsWith(final String prefix) {
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(@Nullable WebDriver driver) {
                return driver.getTitle().startsWith(prefix);
            }
        });
    }
    
    public void waitUntilPageContainsText(final String cssSelector,
                                          final String text) {
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(@Nullable WebDriver driver) {
                List<WebElement> elements = driver
                        .findElements(By.cssSelector(cssSelector));
                
                for(WebElement element : elements) {
                    if (element.isDisplayed() &&
                            element.getText().contains(text)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }
}
