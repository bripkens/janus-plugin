Scenario: Project bootstrap

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <vcsName> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a CI configuration <ciName> is added with the default integration test user data
And the Janus generation is configured
And a JIRA configuration training is added with the default integration test configuration

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