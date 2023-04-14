package telegramBot.repositories;

import telegramBot.entity.Exchange;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRepoImpl implements ExchangeRepo {
    private final SessionFactory sessionFactory = SessionInstance.getInstance();


    @Override
    public void update(Exchange exchange) {
        if(isExist(exchange.getName())) return ;
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.update(exchange);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean isExist(String name){
        Session session = null;
        String result = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        result = (String) session.createSQLQuery("SELECT EXCHANGE_NAME " +
                        "FROM EXCHANGES WHERE EXCHANGE_NAME=:name").
                setParameter("name", name).
                getSingleResult();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
            return false;
        }
    }
    finally {
        if(session != null) session.close();
    }
    return (result != null);
    }

    @Override
    public Exchange getExchangeByName(String name) {
        Session session = null;
        Exchange exchange = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        exchange = (Exchange) session.createSQLQuery("SELECT * FROM EXCHANGES " +
                "WHERE EXCHANGE_NAME=:name").
                setParameter("name", name).
                addEntity(Exchange.class).getSingleResult();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
        }
    }
    finally {
        if(session != null) session.close();
    }
    return exchange;
    }
}
