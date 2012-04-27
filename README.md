# A Simple Jenkins Based Janus Interface

This project provides a user interface for the
[Janus application](https://github.com/bripkens/janus). The user
interface leverages [Jenkins CI](http://jenkins-ci.org/) as a platform to
achieve a seamless integration with existing company toolsets.

# Version History

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