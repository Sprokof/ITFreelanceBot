package telegramBot.repositories;

import telegramBot.entity.InitStatus;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionInstance {
    private static SessionFactory factory;

    public static SessionFactory getInstance(){
        if(factory == null){
            factory = getSessionFactory(new Class[]{User.class, Order.class,
                    telegramBot.entity.Exchange.class,
                    Subscription.class, InitStatus.class});
        }
    return factory;
    }

    private static SessionFactory getSessionFactory(Class<?>[] annotatedClass) {
        Configuration configuration = new Configuration();
        for (Class<?> c : annotatedClass) {
            configuration.addAnnotatedClass(c);
        }
        return configuration.configure("hibernate.cfg.xml").buildSessionFactory();
    }

}
