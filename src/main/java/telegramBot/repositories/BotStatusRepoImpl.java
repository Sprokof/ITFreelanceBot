package telegramBot.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import telegramBot.enums.BotStatus;
import telegramBot.services.BotService;

@Component
public class BotStatusRepoImpl implements BotStatusRepo {
    private final SessionFactory sessionFactory = SessionInstance.getInstance();

    @Override
    public String getStatus() {
        Session session = null;
        String status = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        status = (String) session.createSQLQuery("SELECT STATUS " +
                "FROM BOT_STATUS").getSingleResult();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null) {
            session.getTransaction().rollback();
        }
    }
    finally {
        if(session != null){
            session.close();
        }
    }
    return status;
    }

    @Override
    public void setStatus(String status) {
        Session session = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        session.createSQLQuery("UPDATE BOT_STATUS SET STATUS =: status").
                setParameter("status", status).executeUpdate();
        session.getTransaction().commit();
    }
    catch (Exception e) {
        if (session != null && session.getTransaction() != null) {
            session.close();
        }
    }
    finally {
        if(session != null) session.close();
    }

    }
}
