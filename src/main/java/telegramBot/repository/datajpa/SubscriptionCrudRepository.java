package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionCrudRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.language =:language")
    @EntityGraph(attributePaths = "orders", type = EntityGraph.EntityGraphType.LOAD)
    Subscription getByLanguage(@Param("language") String language);
    @Query("SELECT s FROM Subscription s WHERE s.status =:status")
    @EntityGraph(attributePaths = "orders", type = EntityGraph.EntityGraphType.LOAD)
    List<Subscription> getAllByStatus(@Param("status") SubscriptionStatus status);
    @Query("SELECT s.id FROM Subscription s WHERE s.language =:language")
    int getIdByLanguage(@Param("language") String language);
    @Query(nativeQuery = true, value = "SELECT * FROM Subscription WHERE id IN (SELECT subscription_id " +
            "FROM Users_subscriptions WHERE user_id = (SELECT id FROM Users WHERE chat_id =:chatId))")
    List<Subscription> getAllByUserChatId(@Param("chatId") String chatId);


}
