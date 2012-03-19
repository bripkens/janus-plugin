package de.codecentric.janus.plugin.suite;

import org.junit.Test;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Config {
    private static final String BUNDLE_NAME =
            "de.codecentric.janus.plugin.config";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Config() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
    
    public static String getSeleniumServer() {
        return getString("selenium.server");
    }

    public static boolean isLocalSeleniumExecution() {
        String server = getSeleniumServer();
        return server.contains("local") || server.contains("127.0.0.1");
    }
    
    public static String getSeleniumPort() {
        return getString("selenium.port");
    }

    public static String getJenkinsBaseUrl() {
        return getString("jenkins.base");
    }
    
    public static int getTimeoutInSeconds() {
        return getInt("timeout");
    }
}
