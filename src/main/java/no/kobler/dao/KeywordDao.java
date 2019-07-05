package no.kobler.dao;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import no.kobler.api.Keyword;
import org.hibernate.SessionFactory;

public class KeywordDao extends AbstractDAO<Keyword> {
    @Inject
    public KeywordDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
