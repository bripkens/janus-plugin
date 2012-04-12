package de.codecentric.janus.plugin.library;

import de.codecentric.janus.plugin.suite.Config;
import org.apache.commons.io.FileUtils;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SeleniumAdapter {
    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeStories
    public void initSelenium() throws Exception {
        if (Config.isLocalSeleniumExecution()) {
            driver = new FirefoxDriver();
        } else {
            throw new UnsupportedOperationException("Only local " +
                    "execution is possible.");
        }
    }

    @BeforeScenario
    public void automaticCleanJenkinsConfiguration() throws Exception {
        boolean multipleScenarios = !Config.isSingleExecutionTargetSet();
        if (multipleScenarios) {
            cleanJenkinsConfiguration();
        }
    }

    public void cleanJenkinsConfiguration() throws Exception {
        deleteConfigurationFiles();
        reloadConfiguration();
    }

    private void deleteConfigurationFiles() throws Exception {
        String cwd = System.getProperty("user.dir");
        String jenkinsWorkspace = cwd.replace("janus-plugin-integration-test",
                "janus-plugin" + File.separator + "work");
        String workspaceTemplate = cwd + File.separator  + "src" +
                File.separator + "test" +
                File.separator + "resources" +
                File.separator + "jenkins-workspace-template";

        // delete all configuration data
        FileUtils.deleteDirectory(new File(jenkinsWorkspace));

        // copy config template
        FileUtils.copyDirectory(new File(workspaceTemplate),
                new File(jenkinsWorkspace));
    }

    private void reloadConfiguration() throws Exception {
        assert driver != null;

        int timeoutInSeconds = Config.getTimeoutInSeconds();
        if (Config.isRestartReloadStrategy()) {
            driver.get(Config.getJenkinsBaseUrl() + "restart");
            driver.findElement(By.cssSelector("form[action=\"restart\"] " +
                    "button")).click();
            timeoutInSeconds *= 6;
        } else {
            driver.get(Config.getJenkinsBaseUrl() + "reload");
        }

        new WebDriverWait(driver, timeoutInSeconds)
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(@Nullable WebDriver driver) {
                        return driver.getTitle().startsWith("Dashboard");
                    }
                });
    }

    @AfterStories
    public void afterStories() {
        driver.quit();
    }
}