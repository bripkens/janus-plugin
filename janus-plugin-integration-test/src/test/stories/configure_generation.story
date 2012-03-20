Meta:
@themes Configuration

Scenario: Configure the Janus generation system

Given a clean Jenkins installation
When the Janus generation is configured
Then the Janus generation configuration is persisted