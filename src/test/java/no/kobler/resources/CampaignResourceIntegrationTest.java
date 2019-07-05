package no.kobler.resources;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.CampaignServiceConfiguration;
import no.kobler.IntegrationTestSuite;
import no.kobler.api.CampaignRequest;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class CampaignResourceIntegrationTest {
    private static final DropwizardAppExtension<CampaignServiceConfiguration> APPLICATION = IntegrationTestSuite.APPLICATION;
    private Client client = JerseyClientBuilder.createClient();
    private String uri = String.format("http://localhost:%d/campaigns", APPLICATION.getLocalPort());

    @Test
    void createCampaign() {
        Response response = performCampaignCreationRequest();

        assertEquals(201, response.getStatus());
    }

    @Test
    void getCampaign() {

        performCampaignCreationRequest();

        Response response = client.target(uri + "/1").request().get();

        assertEquals(200, response.getStatus());
    }

    private Response performCampaignCreationRequest() {
        return client.target(uri)
                .request()
                .buildPost(Entity.entity(new CampaignRequest("campaign",
                                Arrays.asList("a", "b"), BigDecimal.ONE),
                        MediaType.APPLICATION_JSON)).invoke();
    }
}
