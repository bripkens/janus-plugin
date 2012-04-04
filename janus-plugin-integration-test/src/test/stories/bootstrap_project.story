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


Scenario: Project bootstrap available scaffolds

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <vcsName> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a configured scaffold directory <scaffoldDir> and catalog <catalog> and working directory <tmpDir>

When the project bootstrap page is accessed

Then the version control system <vcsName> with type <type> can be selected
And the test project scaffolds are visible and can be selected

Examples:
| vcsName       | type      | creationBuild | checkoutBuild   | commitBuild | scaffoldDir                    | catalog                           | tmpDir                     |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild | ./src/test/resources/scaffolds | ./src/test/resources/catalog.json | ./target/applied-scaffolds |