package no.kobler;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;
import no.kobler.core.BidService;
import no.kobler.core.CampaignService;
import no.kobler.dao.CampaignDao;
import no.kobler.health.CampaignHealthCheck;
import no.kobler.resources.BidResource;
import no.kobler.resources.CampaignResource;
import org.hibernate.SessionFactory;

import java.util.Properties;

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
        install(jpaModule(configuration.getDataSourceFactory()));
    }

    private Module jpaModule(DataSourceFactory dataSourceFactory ) {
        final Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.driver", dataSourceFactory.getDriverClass());
        properties.put("javax.persistence.jdbc.url", dataSourceFactory.getUrl());
        properties.put("javax.persistence.jdbc.user", dataSourceFactory.getUser());

        final JpaPersistModule jpaModule = new JpaPersistModule("DefaultUnit");
        jpaModule.properties(properties);

        return jpaModule;
    }
}
