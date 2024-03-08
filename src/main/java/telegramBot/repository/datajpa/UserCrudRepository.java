package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.List;

public interface UserCrudRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.chatId =:chat_id")
    @EntityGraph(attributePaths = "subscriptions", type = EntityGraph.EntityGraphType.LOAD)
    User getByChatId(@Param("chat_id") String chatId);
    @Query("SELECT u FROM User u WHERE u.active = true")
    @EntityGraph(attributePaths = "subscriptions", type = EntityGraph.EntityGraphType.LOAD)
    List<User> getAllActive();
    @Query("SELECT COUNT(u) FROM User u WHERE u.active =:active")
    int countByStatus(@Param("active") boolean active);
    @Query(value = "SELECT COUNT(user_id) FROM users_subscriptions " +
            "AS us WHERE us.subscription_id = " +
            "(SELECT id FROM subscription AS s WHERE s.lang =:language)", nativeQuery = true)
    int countSubscribed(@Param("language") String language);
}
