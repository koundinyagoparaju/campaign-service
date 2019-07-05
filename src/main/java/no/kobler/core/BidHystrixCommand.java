package no.kobler.core;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;
import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.api.Campaign;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
class BidHystrixCommand extends HystrixCommand<Optional<BidResponse>> {
    private BidRequest bidRequest;
    private CampaignService campaignService;
    private SessionFactory sessionFactory;

    BidHystrixCommand(BidRequest bidRequest, CampaignService campaignService, SessionFactory sessionFactory) {
        super(HystrixCommandGroupKey.Factory.asKey("RequestBid"));
        this.bidRequest = bidRequest;
        this.campaignService = campaignService;
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected Optional<BidResponse> run() {
        return HibernateSessionUtil.request(sessionFactory, () -> {
            Optional<Campaign> biddableCampaign = campaignService.getByKeywords(new HashSet<>(bidRequest.getKeywords()))
                    .stream()
                    .distinct()
                    .filter(campaign -> campaign.getSpending().add(BigDecimal.ONE).compareTo(campaign.getBudget()) <= 0)
                    .findAny();
            return biddableCampaign
                    .flatMap(campaign -> campaignService.acceptBid(campaign.getId(), BigDecimal.ONE))
                    .map(campaign -> new BidResponse(bidRequest.getBidId(), BigDecimal.ONE));
        });
    }

    @Override
    protected Optional<BidResponse> getFallback() {
        log.error("falling back on hystrix error");
        return Optional.empty();
    }
}
