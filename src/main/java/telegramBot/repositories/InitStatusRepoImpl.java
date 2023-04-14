package telegramBot.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import telegramBot.entity.InitStatus;

@Component
public class InitStatusRepoImpl implements InitStatusRepo {
    private final SessionFactory sessionFactory = SessionInstance.getInstance();

    @Override
    public InitStatus getStatus() {
        Session session = null;
        InitStatus status = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        status = session.get(InitStatus.class, 1);
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
    return status;
    }

    @Override
    public void update(InitStatus status) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.update(status);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) session.close();
        }
    }
}
