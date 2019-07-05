package no.kobler.resources;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.CampaignServiceConfiguration;
import no.kobler.IntegrationTestSuite;
import no.kobler.api.BidRequest;
import no.kobler.api.CampaignRequest;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class SlowRequestIntegrationTest {
    private static final DropwizardAppExtension<CampaignServiceConfiguration> APPLICATION = IntegrationTestSuite.APPLICATION_WITH_TIMEOUT;
    private Client client = JerseyClientBuilder.createClient();

    @Test
    @Disabled(value = "This is ignored because the latency is less than ms while running in mvn test")
    void bidFailureDueToTimeout() {
        String uri = String.format("http://localhost:%d", APPLICATION.getLocalPort());

        createCampaign(uri);
        Response bid = createBid(uri);

        assertEquals(204, bid.getStatus());
    }

    private Response createBid(String uri) {
        return client.target(uri + "/bids").request().buildPost(Entity.entity(new BidRequest(1L,
                        Arrays.asList("a")),
                APPLICATION_JSON))
                .invoke();
    }

    private void createCampaign(String baseUri) {
        client.target(baseUri + "/campaigns").request()
                .buildPost(Entity.entity(new CampaignRequest("campaign",
                                Arrays.asList("a", "b"),
                                BigDecimal.ONE),
                        APPLICATION_JSON))
                .invoke();
    }
}
