Meta:
@themes Configuration

Scenario: Add VCS configurations for a new system

Given an installation with three builds named <creationBuild>, <checkoutBuild> and <commitBuild>
When a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>
Then a <type> installation <name> can be found

Examples:
 
| name          | type      | creationBuild | checkoutBuild   | commitBuild |
| OurDetaultVCS | MERCURIAL | creationBuild | checkoutBuild   | commitBuild |

