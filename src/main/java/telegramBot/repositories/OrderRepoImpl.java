package telegramBot.repositories;

import telegramBot.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

@Component
public class OrderRepoImpl implements OrderRepo {
    private final SessionFactory sessionFactory = SessionInstance.getInstance();

    @Override
    public void saveOrder(Order order) {
        Session session = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        session.save(order);
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
    public boolean exist(String orderLink) {
        Session session = this.sessionFactory.openSession();
        boolean exist = false;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        exist = (Boolean) session.createSQLQuery("SELECT EXISTS " +
                "(SELECT * FROM ORDERS WHERE ORDER_LINK=:link)").
                setParameter("link", orderLink).getSingleResult();
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

    return exist;

    }

    @Override
    public void update(Order order) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.update(order);
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
    public List<Order> getOrdersByLanguage(Language language) {
        Session session = null;
        List<Order> orders = null;
    try {
        session = this.sessionFactory.openSession();
        session.beginTransaction();
        orders = (List<Order>) session.createSQLQuery("SELECT * FROM ORDERS as o JOIN SUBSCRIPTIONS as s on " +
                "o.subscription_id=s.id WHERE s.SUB_LANGUAGE=:language order by o.id desc").
                setParameter("language", language.getName()).
                addEntity(Order.class).list();
        session.getTransaction().commit();
    }
    catch(Exception e){
        if(session != null && session.getTransaction() != null){
            session.getTransaction().rollback();
        }
    }
    finally {
        if(session != null) session.close();
    }
    return orders;
    }


    @Override
    public Order getOrderByLink(String link) {
        Session session = null;
        Order order = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            order = (Order) session.createSQLQuery("SELECT * FROM ORDERS " +
                            "WHERE ORDER_LINK=:link").
                    setParameter("link", link).addEntity(Order.class).
                    getSingleResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) session.close();
        }
        return order;
    }


    @Override
    public void deleteExchangeOrders(Exchange exchange) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM ORDERS WHERE exchange_id IN " +
                    "(SELECT ID FROM EXCHANGES WHERE EXCHANGE_NAME =:name) and " +
                            "INIT_DATE <= cast(:date as date)").
                    setParameter("name", exchange.getName()).setParameter("date",
                    currentDateMinusExchangeRefreshInterval(exchange.getRefreshInterval())).
                    executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) session.close();
        }
    }


    private String currentDateMinusExchangeRefreshInterval(int interval){
        return LocalDate.now().minusDays(interval).toString();
    }

    @Override
    public void deleteOrderById(long id) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery("DELETE FROM " +
                    "ORDERS WHERE ID=:id").setParameter("id", id).executeUpdate();
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
