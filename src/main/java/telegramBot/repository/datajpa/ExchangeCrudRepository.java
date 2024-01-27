package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Exchange;

public interface ExchangeCrudRepository extends CrudRepository<Exchange, Long> {
    @Query("SELECT COUNT(e) FROM Exchange e WHERE e.name =:name")
    int existByName(@Param("name") String name);
    @Query("SELECT e FROM Exchange e WHERE e.name =:name")
    @EntityGraph(attributePaths = "orders", type = EntityGraph.EntityGraphType.LOAD)
    Exchange getByName(@Param("name") String name);
}
