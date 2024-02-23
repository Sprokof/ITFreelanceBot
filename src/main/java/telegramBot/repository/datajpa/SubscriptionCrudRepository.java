package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Subscription;
import telegramBot.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionCrudRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.language =:language")
    @EntityGraph(attributePaths = "orders", type = EntityGraph.EntityGraphType.LOAD)
    Subscription getByLanguage(@Param("language") String language);

    @Query("SELECT s FROM Subscription s WHERE s.status =:status")
    @EntityGraph(attributePaths = "orders", type = EntityGraph.EntityGraphType.LOAD)
    List<Subscription> getAllByStatus(@Param("status") SubscriptionStatus status);

}
