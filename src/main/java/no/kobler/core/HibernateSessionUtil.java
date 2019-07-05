package no.kobler.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

import java.util.function.Supplier;

final class HibernateSessionUtil {

    private HibernateSessionUtil() {

    }

    static <T> T request(SessionFactory factory, Supplier<T> request) {
        Transaction txn = null;
        Session session = factory.openSession();
        try {
            ManagedSessionContext.bind(session);
            txn = session.beginTransaction();
            T result = request.get();
            commit(txn);
            return result;
        } catch (Throwable th) {
            rollback(txn);
            throw th;
        } finally {
            ManagedSessionContext.unbind(factory);
        }
    }

    private static void rollback(Transaction txn) {
        if (txn != null && txn.isActive()) {
            txn.rollback();
        }
    }

    private static void commit(Transaction txn) {
        if (txn != null && txn.isActive()) {
            txn.commit();
        }
    }

}
