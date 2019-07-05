package no.kobler.core;


import no.kobler.api.Campaign;
import no.kobler.dao.CampaignDao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CampaignServiceTest {
    private CampaignService campaignService;

    @Test
    public void shouldRateLimitBids() {
        CampaignDao campaignDao = mock(CampaignDao.class);
        campaignService = new CampaignService(campaignDao);
        Campaign campaign = new Campaign("campaign-one", BigDecimal.valueOf(100), BigDecimal.valueOf(0));
        campaign.setId(1L);
        when(campaignDao.getById(1L)).thenReturn(campaign);
        when(campaignDao.save(any(Campaign.class))).thenReturn(campaign.getId());

        long successFulBids = IntStream.range(1, 15)
                .boxed()
                .map(i -> campaignService.acceptBid(campaign.getId(), BigDecimal.ONE))
                .filter(Optional::isPresent)
                .count();
        assertEquals(10, successFulBids);
    }

    @Test
    public void shouldNotAcceptBidsExceedingBudget() {
        CampaignDao campaignDao = mock(CampaignDao.class);
        campaignService = new CampaignService(campaignDao);
        Campaign campaign = new Campaign("campaign-one", BigDecimal.valueOf(9), BigDecimal.valueOf(0));
        campaign.setId(1L);
        when(campaignDao.getById(1L)).thenReturn(campaign);
        when(campaignDao.save(any(Campaign.class))).thenReturn(campaign.getId());

        long successFulBids = IntStream.range(1, 10)
                .boxed()
                .map(i -> campaignService.acceptBid(campaign.getId(), BigDecimal.ONE))
                .filter(Optional::isPresent)
                .count();
        assertEquals(9, successFulBids);
    }
}
