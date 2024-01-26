package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Subscription;

public interface SubscriptionCrudRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.lang =:language")
    Subscription getByLanguage(@Param("language") String language);
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.lang =:lang AND s.user.chatId =:chat_id")
    int existByLanguageAndChatId(@Param("lang") String language, @Param("chat_id") String chatId);
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.user =:u_id")
    int exists(@Param("u_id") long userId);
    @Modifying
    @Query("DELETE FROM User_subscriptions s WHERE s.subscription_id =:id AND s.user_id =:u_id")
    int delete(@Param("id") long id, @Param("user_id") long userId);
}
