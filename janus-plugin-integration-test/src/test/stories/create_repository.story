Meta:
@themes Create Repository

Scenario: Add a repository

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
And a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>

When a repository <repositoryName> is created for vcs <name>

Then the repository creation success page is shown
And the build <creationBuild> is successfully executed

Examples:

| name          | repositoryName | type      | creationBuild | checkoutBuild   | commitBuild |
| OurDefaultVCS | newRepo        | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |

