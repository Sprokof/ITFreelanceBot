package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.User;

import java.util.List;

public interface UserCrudRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.chatId =:chat_id")
    @EntityGraph(attributePaths = "subscriptions", type = EntityGraph.EntityGraphType.LOAD)
    User getByChatId(@Param("chat_id") String chatId);
    @Query("SELECT u FROM User u WHERE u.active = true")
    @EntityGraph(attributePaths = "subscriptions", type = EntityGraph.EntityGraphType.LOAD)
    List<User> getAllActive();
}
