package telegramBot.repositories;

import telegramBot.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.List;

@Component
public class UserRepoImpl implements UserRepo {

    private final SessionFactory sessionFactory = SessionInstance.getInstance();

    @Override
    public void saveUser(User user) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
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
    public User getUserByChatId(String chatId) {
        Session session = null;
        User user = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            user = (User) session.createSQLQuery("SELECT * FROM USERS " +
                            "WHERE CHAT_ID=:id").
                    setParameter("id", chatId).
                    addEntity(User.class).getSingleResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
                if (e instanceof NoResultException) return null;
            }
        } finally {
            if (session != null) session.close();
        }
        return user;

    }

    @Override
    public void update(User user) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.update(user);
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
    public boolean exist(User user) {
        Session session = null;
        Integer id = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        id = (Integer) session.createSQLQuery("SELECT ID FROM " +
                "USERS WHERE CHAT_ID=:id").
                setParameter("id", user.getChatId()).getSingleResult();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
            if(e instanceof NoResultException) return false;
        }
    }
    finally {
        if(session != null) session.close();
    }
    return (id != null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllActiveUsers() {
        Session session = null;
        List<User> chatsIds = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        chatsIds = (List<User>) session.createSQLQuery("SELECT * FROM USERS WHERE ACTIVE IS TRUE").
                addEntity(User.class).list();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
        }
    }
    finally {
        if(session != null){
            session.close();
        }
    }
    return chatsIds;
    }
}
