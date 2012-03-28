Scenario: Project bootstrap available scaffolds

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
And a configured scaffold directory <scaffoldDir> and catalog <catalog>

When the project bootstrap page is accessed

Then the version control system <name> with type <type> can be selected
And the test project scaffolds are visible and can be selected

Examples:
| name          | type      | creationBuild | checkoutBuild   | commitBuild | scaffoldDir                    | catalog                           |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild | ./src/test/resources/scaffolds | ./src/test/resources/catalog.json |