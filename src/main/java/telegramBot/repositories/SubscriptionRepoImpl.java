package telegramBot.repositories;

import org.checkerframework.checker.units.qual.A;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Component
public class SubscriptionRepoImpl implements SubscriptionRepo {

    private final SessionFactory sessionFactory = SessionInstance.getInstance();

    @Override
    public void addSubscription(User user, Language language) {
        Session session = null;
        user.addSubscription(getSubscriptionByLanguage(language));
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        session.update(user);
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
    }

    @Override
    public Subscription getSubscriptionByLanguage(Language language) {
        Session session = null;
        Subscription subscription = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            subscription = (Subscription) session.createSQLQuery("SELECT * FROM SUBSCRIPTIONS " +
                            "WHERE SUB_LANGUAGE=:lan").
                    setParameter("lan", language.getName()).
                    addEntity(Subscription.class).getSingleResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                if (e instanceof NoResultException) return null;
            }
        } finally {
            if (session != null) session.close();
        }
        return subscription;
    }

    @Override
    public boolean exist(Subscription subscription) {
        Session session = null;
        String lan = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        lan = (String) session.createSQLQuery("SELECT SUB_LANGUAGE FROM " +
                "SUBSCRIPTIONS WHERE SUB_LANGUAGE=:lan").
                setParameter("lan", subscription.getLanguage()).
                getSingleResult();
        session.getTransaction().commit();
    }
    catch (Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
            if(e instanceof NoResultException) return false;
        }
    }
    finally {
        if(session != null){
            session.close();
        }
    }
    return (lan != null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Subscription> getSubscriptions() {
        Session session = null;
        List<Subscription> subscriptions = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        subscriptions = (List<Subscription>)
                session.
                        createSQLQuery("SELECT * FROM SUCBSCRIPTIONS").
                        addEntity(Subscription.class).list();
        session.getTransaction().commit();
    }
    catch(Exception e) {
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
        }
    }
    finally {
        if(session != null) session.close();
    }
    return subscriptions;
    }

    @Override
    public void update(Subscription subscription) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.update(subscription);
            session.getTransaction().commit();
        }
        catch(Exception e){
            if(session != null && session.getTransaction() != null){
                session.getTransaction().rollback();
            }
        }
        finally {
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean subscriptionsExists() {
        Session session = null;
        BigInteger count = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            count = (BigInteger)
                    session.createSQLQuery("SELECT COUNT(USER_ID) FROM USERS_SUBSCRIPTIONS").getSingleResult();
            session.getTransaction().commit();
        }
        catch(Exception e) {
            if(session != null && session.getTransaction() != null){
                session.getTransaction().rollback();
                if(e instanceof NoResultException) return false;
            }
        }
        finally {
            if(session != null) session.close();
        }

        assert count != null;
        return count.intValue() > 0;
    }

    @Override
    public void deleteSubscription(int userId, int subId) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM USERS_SUBSCRIPTIONS WHERE " +
                    "USER_ID=:uId and SUBSCRIPTION_ID=:sId").
                    setParameter("uId", userId).
                    setParameter("sId", subId).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) session.getTransaction();
        }
    }

    private Integer getId(String language){
        Session session = null;
        Integer id = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        id = (Integer) session.createSQLQuery("SELECT ID FROM " +
                "SUBSCRIPTIONS WHERE SUB_LANGUAGE=:lan").
                setParameter("lan", language).getSingleResult();
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
    return id;
    }
}
