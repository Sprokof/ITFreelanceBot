package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Exchange;

public interface ExchangeCrudRepository extends CrudRepository<Exchange, Long> {

    @Query("SELECT COUNT(e) FROM Excahnge e WHERE e.name =:name")
    int existByName(@Param("name") String name);
    @Query("SELECT e FROM Excahnge e WHERE e.name =:name")
    Exchange getByName(@Param("name") String name);
}
