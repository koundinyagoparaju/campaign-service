package no.kobler.core;

import com.google.inject.Inject;
import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class BidService {

    private CampaignService campaignService;
    private SessionFactory sessionFactory;

    @Inject
    public BidService(CampaignService campaignService, SessionFactory sessionFactory) {
        this.campaignService = campaignService;
        this.sessionFactory = sessionFactory;
    }

    public Optional<BidResponse> bid(BidRequest bidRequest) {
        return new BidHystrixCommand(bidRequest, campaignService, sessionFactory).execute();
    }
}




