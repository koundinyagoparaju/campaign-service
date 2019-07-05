package no.kobler.resources;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.CampaignServiceConfiguration;
import no.kobler.IntegrationTestSuite;
import no.kobler.api.BidRequest;
import no.kobler.api.CampaignRequest;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class BidResourceIntegrationTest {
    private static final DropwizardAppExtension<CampaignServiceConfiguration> APPLICATION = IntegrationTestSuite.APPLICATION;

    private static Client client = JerseyClientBuilder.createClient();
    private String uri = String.format("http://localhost:%d", APPLICATION.getLocalPort());

    @Test
    void placeSuccessfulBid() {
        createCampaign(uri);

        Response firstBid = createBid(uri);
        assertEquals(200, firstBid.getStatus());
        Response secondBid = createBid(uri);
        assertEquals(204, secondBid.getStatus());
    }

    @Test
    void throttlingMoreThan10BidRequestsPer10Seconds() {
        createCampaign(uri);
        IntStream.range(1, 10).boxed().forEach(i -> createBid(uri));
        Response nextBid = createBid(uri);

        assertEquals(204, nextBid.getStatus());
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
