package no.kobler.core;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import no.kobler.api.Campaign;
import no.kobler.api.CampaignRequest;
import no.kobler.api.Keyword;
import no.kobler.dao.CampaignDao;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CampaignService {
    private CampaignDao campaignDao;

    @Inject
    public CampaignService(CampaignDao campaignDao) {
        this.campaignDao = campaignDao;
    }

    private RateLimiter rateLimiter = new RateLimiter(10, 10);

    public Optional<Campaign> getById(Long id) {
        try{
            return Optional.ofNullable(campaignDao.getById(id));
        } catch (Exception e) {
            log.error(String.format("unable to fetch campaign for id: %d", id), e);
            return Optional.empty();
        }
    }

    public Optional<Long> create(CampaignRequest campaignRequest) {
        try{
            Campaign campaign = new Campaign(campaignRequest.getName(), campaignRequest.getBudget(), BigDecimal.ZERO);
            campaign.setKeywords(campaignRequest.getKeywords()
                    .stream()
                    .map(keywordText -> new Keyword(keywordText, campaign))
                    .collect(Collectors.toList()));
            return Optional.of(campaignDao.save(campaign));
        } catch (Exception e) {
            log.error("unable to create campaigns", e);
            return Optional.empty();
        }
    }

    public List<Campaign> getAll() {
        try {
            return campaignDao.getAll();
        } catch (Exception e) {
            log.error("unable to fetch campaigns", e);
            return Collections.emptyList();
        }
    }

    public List<Campaign> getByKeywords(Set<String> keywords) {
        try {
            return campaignDao.containingKeywords(keywords);
        } catch (Exception e) {
            log.error("unable to fetch campaigns by keywords", e);
            return Collections.emptyList();
        }
    }

    Optional<Campaign> acceptBid(Long campaignId, BigDecimal bidAmount) {
        try {
            if(!rateLimiter.canAdd()){
                return Optional.empty();
            }
            Campaign campaign = campaignDao.getById(campaignId);
            if(campaign.getBudget().compareTo(campaign.getSpending().add(bidAmount)) < 0) {
                log.warn(String.format("bidAmount exceeded budget for campaign campaignId: %d", campaignId));
                return Optional.empty();
            }
            campaign.setSpending(campaign.getSpending().add(bidAmount));
            campaignDao.save(campaign);
            return Optional.of(campaign);
        } catch (Exception e) {
            log.error("unable to add bidAmount", e);
            return Optional.empty();
        }
    }
}
