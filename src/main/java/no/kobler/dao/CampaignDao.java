package no.kobler.dao;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import no.kobler.api.Campaign;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class CampaignDao extends AbstractDAO<Campaign> {
    @Inject
    public CampaignDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Campaign getById(Long id) {
        return get(id);
    }

    public Long save(Campaign campaign) {
        return persist(campaign).getId();
    }

    public List<Campaign> getAll() {
        return list((Query<Campaign>) namedQuery("no.kobler.api.Campaign.findAll"));
    }

    public List<Campaign> containingKeywords(Set<String> keywords) {
        return list((Query<Campaign>) namedQuery("no.kobler.api.Campaign.findByKeywords").setParameter("keywords", keywords));
    }
}
