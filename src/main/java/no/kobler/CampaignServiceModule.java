package no.kobler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;
import no.kobler.core.BidService;
import no.kobler.core.CampaignService;
import no.kobler.dao.CampaignDao;
import no.kobler.health.CampaignHealthCheck;
import no.kobler.resources.BidResource;
import no.kobler.resources.CampaignResource;
import org.hibernate.SessionFactory;

public class CampaignServiceModule extends AbstractModule {

    private CampaignServiceConfiguration configuration;
    private Environment environment;
    private HibernateBundle hibernateBundle;

    public CampaignServiceModule(final CampaignServiceConfiguration configuration, final Environment environment, final HibernateBundle hibernateBundle) {
        this.configuration = configuration;
        this.environment = environment;
        this.hibernateBundle = hibernateBundle;
    }

    @Provides
    @Singleton
    public SessionFactory hibernateSessionFactory() {
       return hibernateBundle.getSessionFactory();
    }

    @Override
    protected void configure() {
        bind(CampaignServiceConfiguration.class).toInstance(configuration);
        bind(CampaignService.class).asEagerSingleton();
        bind(CampaignResource.class).asEagerSingleton();
        bind(Environment.class).toInstance(environment);
        bind(BidResource.class).asEagerSingleton();
        bind(BidService.class).asEagerSingleton();
        bind(CampaignDao.class).asEagerSingleton();
        bind(CampaignHealthCheck.class).asEagerSingleton();
    }

}
