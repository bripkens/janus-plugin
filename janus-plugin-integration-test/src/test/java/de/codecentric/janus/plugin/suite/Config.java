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

package de.codecentric.janus.plugin.suite;

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

    public static boolean isRestartReloadStrategy() {
        // matching against reload, because restart should be the default.
        // reloading is problematic because not all descriptors are actually
        // reloaded!
        return !getString("reload.strategy").equalsIgnoreCase("reload");
    }
    
    public static String getSingleExecutionTarget() {
        return getString("execute.single");
    }

    public static boolean isSingleExecutionTargetSet() {
        return !getSingleExecutionTarget().isEmpty();
    }

    public static int getTimeoutInSeconds() {
        return getInt("timeout");
    }

    public static String getDefaultUsername() {
        return getString("auth.username");
    }

    public static String getDefaultPassword() {
        return getString("auth.password");
    }

    public static String getDefaultApiToken() {
        return getString("auth.apiToken");
    }
}
