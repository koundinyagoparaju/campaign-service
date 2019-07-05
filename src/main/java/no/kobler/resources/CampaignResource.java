package no.kobler.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import no.kobler.api.CampaignRequest;
import no.kobler.core.CampaignService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;

@Path("/campaigns")
@Produces(APPLICATION_JSON)
public class CampaignResource {
    private CampaignService campaignService;

    @Inject
    public CampaignResource(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response get(@PathParam("id") Long campaignId) {
        return campaignService.getById(campaignId).map(Response::ok).orElse(status(NOT_FOUND)).build();
    }

    @GET
    @UnitOfWork
    @Timed
    public Response get() {
        return Response.ok(campaignService.getAll()).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @UnitOfWork
    @Timed
    public Response post(CampaignRequest campaignRequest) {
        return campaignService.create(campaignRequest)
                .map(campaignId -> Response.created(UriBuilder.fromPath("/campaigns/{campaignId}").build(campaignId)))
                .orElse(status(INTERNAL_SERVER_ERROR)).build();
    }
}
