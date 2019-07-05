package no.kobler.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import no.kobler.api.BidRequest;
import no.kobler.core.BidService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/bids")
@Produces(APPLICATION_JSON)
public class BidResource {
    private BidService bidService;

    @Inject
    public BidResource(BidService bidService) {
        this.bidService = bidService;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Timed
    public Response post(BidRequest bidRequest) {
        return bidService.bid(bidRequest).map(Response::ok).orElse(Response.noContent()).build();
    }
}
