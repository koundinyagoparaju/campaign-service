package no.kobler.health;

import com.google.inject.Inject;
import no.kobler.core.CampaignService;

import static com.codahale.metrics.health.HealthCheck.Result.healthy;
import static com.codahale.metrics.health.HealthCheck.Result.unhealthy;

public class CampaignHealthCheck extends com.codahale.metrics.health.HealthCheck {
    private CampaignService campaignService;

    @Inject
    public CampaignHealthCheck(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    protected Result check() {
        return isDatabaseUp() ? healthy() : unhealthy("database is down");
    }

    private boolean isDatabaseUp() {
        try {
            campaignService.getAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
