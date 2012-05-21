# A Simple Jenkins Based Janus Interface

This project provides a user interface for the
[Janus application](https://github.com/bripkens/janus). The user
interface leverages [Jenkins CI](http://jenkins-ci.org/) as a platform to
achieve a seamless integration with existing company toolsets.

# Integration Test Configuration

JIRA and Confluence need to be running for the integration tests. Both should
share the user database and accessible over locahost. JIRA should be running
under localhost:47623 and Confluence under localhost:47624. The admin account
should have the name `janustestadmin` with password `janusRocks`.

# Deployment

## Prerequisites

The plug-in may be able to work properly with lower versions than the ones
listed in this section. Janus was tested in the following environment:

 - Jenkins 1.452
 - JIRA 5
 - Confluence 4.2

Please make sure that the Mercurial plug-in is installed in Jenkins. To compile
Janus and its dependencies you will need [Gradle](http://www.gradle.org/) and
[Maven](http://maven.apache.org/) as well as JDK >= 1.6.
[JIRA](http://confluence.atlassian.com/display/JIRA042/JIRA+XML-RPC+Overview)
and [Confluence](http://confluence.atlassian.com/display/DOC/Enabling+the+Remote+API)
SOAP and REST web services need to be activated in the applications'
administration menus.

## Compilation

    # First, dependencies need to be installed locally.
    wget -O janus.tar.gz https://github.com/bripkens/janus/tarball/master
    tar xfz janus.tar.gz
    cd bripkens-janus-*
    gradle check install
    cd ..
    rm -rf janus.tar.gz bripkens-janus-*

    # Now the actual plug-in can be compiled.
    wget -O janus-plugin.tar.gz https://github.com/bripkens/janus-plugin/tarball/master
    tar xfz janus-plugin.tar.gz
    cd bripkens-janus-plugin-*

    # Make sure to skip the tests to avoid integration test execution
    mvn package -DskipTests

## Installation

Go to your Jenkins installation plug-in administration page. This page is
accessible through `Manage Jenkins -> Manage Plugins -> Advanced`. The right
location can typically be verified by inspecting the URL - the URL should end
with `pluginManager/advanced`.

On this page, upload the plug-in manually by selecting the result of the
previous compilation step. The plug-in file is located at
`janus-plugin/target/janus-plugin.hpi`.

## Configuration
Once the plug-in is installed, you need to configure Janus before you can use
it. In Jenkins' standard configuration menu (`Manage Jenkins ->
Configure System`), you can configure all Janus parts. In this menu and next
to each configuration option, you will find additional information which
further guides you through the configuration process. After configuration,
you can select the `Bootstrap Project` option from Jenkins' main menu to use
Janus.


# Version History

## 0.5

 - configuration menu support for JIRA and Confluence (UC-3).
 - project bootstrap extended with support for automatic JIRA and Confluence
   configuration (UC-11).

## 0.4

 - configuration menu now supports configuration of continuous integration (CI)
   systems (UC-2).
 - VCS configuration was extended with a new option *checkout URL*, i.e., a
   URL which can be used by a continuous integration system to check out
   source code (UC-2).
 - in the project bootstrap view a configured CI system can be selected
   (currently only Jenkins CI) which will then be used as the target CI for
   the build jobs (UC-10).

## 0.3

 - integration test harness using jBehave and Selenium 2.
 - additional configuration options to locate the scaffold catalog, the general
   scaffold directory as well as a working directory (UC-5).
 - new view to bootstrap projects. It initiallity supports repository creation
   and application of project scaffolds. The bootstrap results are shown in
   form of a log (UC-9).

## 0.2

 - configuration dialog overhauled with proper validation and addition of
   multiple version control systems (UC-1).
 - view to create new repositories (UC-7).

## 0.1

 - initial project structure.
 - basic configuration dialog.

# License

Copyright (C) 2012 codecentric AG, Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).