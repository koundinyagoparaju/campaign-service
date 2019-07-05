package no.kobler;

import io.dropwizard.testing.junit5.DropwizardAppExtension;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

public class IntegrationTestSuite {
        public static final DropwizardAppExtension<CampaignServiceConfiguration> APPLICATION  = new DropwizardAppExtension<>(CampaignServiceApplication.class, resourceFilePath("config.yml"));
        public static final DropwizardAppExtension<CampaignServiceConfiguration> APPLICATION_WITH_TIMEOUT  = new DropwizardAppExtension<>(CampaignServiceApplication.class, resourceFilePath("timeout-test-config.yml"));
}
