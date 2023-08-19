package telegramBot.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import telegramBot.entity.BotStatus;
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
        status = ((BotStatus) session.createSQLQuery("SELECT * " +
                "FROM BOT_STATUS").addEntity(BotStatus.class).
                getSingleResult()).getStatus();
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
