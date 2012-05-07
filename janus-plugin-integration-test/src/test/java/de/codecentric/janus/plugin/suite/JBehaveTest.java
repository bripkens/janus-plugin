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

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import org.jbehave.core.steps.guice.GuiceStepsFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JBehaveTest extends JUnitStories {

    public JBehaveTest() {
        configuredEmbedder().embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(false)
                .useThreads(1)
                .useStoryTimeoutInSecs(60 * 5);
    }

    private Injector createInjector() {
        return Guice.createInjector(new StepsModule());
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters = new ParameterConverters();
        // factory to allow parameter conversion and loading from external
        // resources (used by StoryParser too)
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(
                new LocalizedKeywords(),
                new LoadFromClasspath(embeddableClass), parameterConverters);
        // add custom coverters
        parameterConverters.addConverters(new DateConverter(
                new SimpleDateFormat("yyyy-MM-dd")),
                new ExamplesTableConverter(examplesTableFactory));
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
                                .withCodeLocation(
                                        CodeLocations
                                                .codeLocationFromClass(embeddableClass))
                                .withDefaultFormats()
                                .withFormats(CONSOLE, TXT, HTML, XML))
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
                .useParameterConverters(parameterConverters);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new GuiceStepsFactory(configuration(), createInjector());
    }

    private static void recastExceptionWhenFindingSteps(Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Could not find Step classes", e);
    }

    @Override
    protected List<String> storyPaths() {
        String singleStoryName = Config.getSingleExecutionTarget();
        String includePattern = "**/*.story";
        if (singleStoryName != null && singleStoryName.endsWith(".story")) {
            includePattern = "**/" + singleStoryName;
        }
        return new StoryFinder().findPaths(
                codeLocationFromClass(this.getClass()), includePattern,
                "**/excluded*.story");
    }

    /**
     * Defines the classes that contain the Steps of the Scenarios.
     */
    public static class StepsModule extends AbstractModule {

        @Override
        protected void configure() {
            List<String> stepNames = new StoryFinder().findPaths(
                    codeLocationFromClass(this.getClass()),
                    "**/step/**/*.class", "");
            stepNames.addAll(new StoryFinder().findPaths(
                    codeLocationFromClass(this.getClass()),
                    "**/library/**/*.class", ""));
            try {
                for (String stepName : stepNames) {

                    String className = convertFilePathToClassName(stepName);
                    if (!stepName.contains("$")) {
                        bind(Class.forName(className)).in(Scopes.SINGLETON);
                    }
                }
            } catch (ClassNotFoundException e) {
                recastExceptionWhenFindingSteps(e);
            }
        }

        private String convertFilePathToClassName(String stepName) {
            return stepName.replaceAll("/", ".").substring(0,
                    stepName.length() - ".class".length());
        }
    }

}