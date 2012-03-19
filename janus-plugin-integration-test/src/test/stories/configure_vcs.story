Meta:
@themes Configuration

Scenario: Add VCS configurations for a new system

Given a clean Jenkins installation
And a build <creationBuild>
And a build <checkoutBuild>
And a build <commitBuild>
When a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
Then a <type> installation <name> can be found

Examples:
 
| name          | type      | creationBuild | checkoutBuild   | commitBuild |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |
| AnotherVCS    | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |

