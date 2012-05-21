Meta:
@themes Configuration

Scenario: Configure the Janus JIRA system

Given a clean Jenkins installation
When the JIRA system is configured with name Training
Then the Janus JIRA configuration is persisted