package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderCrudRepository extends CrudRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.link =:link")
    int exist(@Param("link") String link);
    @Query("SELECT o FROM Orders o WHERE o.link =:link")
    Order getByLink(@Param("link") String link);
    @Modifying
    @Query("DELETE FROM Orders o WHERE o.exchange.id IN " +
            "(SELECT e.id FROM Exchange e WHERE e.name =:name) and o.initDate <=:date")
    int deleteByExchangeAndDate(@Param("exchange") String exchange, @Param("date") LocalDate date);
    @Modifying
    @Query("DELETE FROM Orders o WHERE o.id=:id")
    int delete(long id);
    @Query("SELECT o FROM Orders o WHERE o.subscription.language =:language")
    List<Order> getAllByLanguage(String language);
}
