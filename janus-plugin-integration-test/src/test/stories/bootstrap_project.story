Meta:
@themes Create Project

Scenario: Jenkins is not properly configured

Given a clean Jenkins installation
When the project bootstrap page is accessed
Then an invalid configuration error message is shown




Scenario: Jenkins only contains repository configurations

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>

When the project bootstrap page is accessed

Then an invalid configuration error message is shown

Examples:
| name          | type      | creationBuild | checkoutBuild   | commitBuild |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |




Scenario: Jenkins contains no CI configuration

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a configured scaffold directory <scaffoldDir> and catalog <catalog> and working directory <tmpDir>

When the project bootstrap page is accessed

Then an invalid configuration error message is shown

Examples:
| vcsName       | type      | creationBuild | checkoutBuild   | commitBuild | scaffoldDir                    | catalog                           | tmpDir                     |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild | ./src/test/resources/scaffolds | ./src/test/resources/catalog.json | ./target/applied-scaffolds |




Scenario: Project bootstrap available scaffolds

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <vcsName> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a configured scaffold directory <scaffoldDir> and catalog <catalog> and working directory <tmpDir>
And a CI configuration <ciName> is added with the default integration test user data

When the project bootstrap page is accessed

Then the version control system <vcsName> with type <type> can be selected
And the test project scaffolds are visible and can be selected

Examples:
| vcsName       | type      | creationBuild | checkoutBuild   | commitBuild | scaffoldDir                    | catalog                           | tmpDir                     |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild | ./src/test/resources/scaffolds | ./src/test/resources/catalog.json | ./target/applied-scaffolds |




Scenario: Project bootstrap

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <vcsName> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a CI configuration <ciName> is added with the default integration test user data
And the Janus generation is configured

When a project <projectName> with package <package> and VCS <vcsName> is bootstrapped

Then the bootstrap success message is shown
And no failure log message is shown
And the build <creationBuild> is successfully executed
And the build <checkoutBuild> is successfully executed
And the build <commitBuild> is successfully executed
And the target directory contains the scaffold named <projectName>
And the pom.xml file contains the artifactId <projectName> and groupId <package>
And the build Worblehat-build is created
And the build Worblehat-deploy-staging is created

Examples:
| projectName | package                  | vcsName          | type      | creationBuild | checkoutBuild   | commitBuild |
| Worblehat   | de.codecentric.worblehat | OurDetaultVCS    | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |