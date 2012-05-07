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

package de.codecentric.janus.plugin.vcs;

import hudson.model.Action;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RepositoryCreationSuccessAction implements Action {
    public static final String URL = "success";
    
    public String getIconFileName() {
        return "gear.png";
    }

    public String getDisplayName() {
        return "Repository successfully created";
    }

    public String getUrlName() {
        return URL;
    }
}
