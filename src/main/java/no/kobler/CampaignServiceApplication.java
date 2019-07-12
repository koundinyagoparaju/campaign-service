package no.kobler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.kobler.api.Campaign;
import no.kobler.api.Keyword;
import no.kobler.health.CampaignHealthCheck;
import no.kobler.resources.BidResource;
import no.kobler.resources.CampaignResource;
import org.apache.commons.configuration.MapConfiguration;
import org.flywaydb.core.Flyway;

public class CampaignServiceApplication extends Application<CampaignServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CampaignServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "CampaignService";
    }

    @Override
    public void initialize(final Bootstrap<CampaignServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    private final HibernateBundle<CampaignServiceConfiguration> hibernate = new HibernateBundle<CampaignServiceConfiguration>(Campaign.class, Keyword.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(CampaignServiceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void run(final CampaignServiceConfiguration configuration,
                    final Environment environment) {
        new Flyway(Flyway.configure().dataSource(configuration.getDataSourceFactory().getUrl(),
                configuration.getDataSourceFactory().getUser(),
                configuration.getDataSourceFactory().getPassword()))
                .migrate();
        if(!ConfigurationManager.isConfigurationInstalled()) {
            ConfigurationManager.install(new MapConfiguration(configuration.getDefaultHystrixConfig()));
        }
        final Injector injector = Guice.createInjector(new CampaignServiceModule(configuration, environment, hibernate));

        environment.healthChecks().register("campaigns", injector.getInstance(CampaignHealthCheck.class));
        environment.jersey().register(injector.getInstance(CampaignResource.class));
        environment.jersey().register(injector.getInstance(BidResource.class));
    }

}
