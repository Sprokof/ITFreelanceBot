package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.time.LocalDate;
import java.util.List;

public interface OrderCrudRepository extends CrudRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.link =:link")
    int existByLink(@Param("link") String link);
    @Modifying
    @Query("DELETE FROM Order o WHERE o.exchange.id IN (SELECT e.id FROM Exchange e " +
            "WHERE e.name =:exchange) and o.initDate <=:date")
    int deleteByExchangeAndDate(@Param("exchange") String exchange, @Param("date") LocalDate date);
    @Modifying
    @Query("DELETE FROM Order o WHERE o.id =:id")
    int delete(long id);
    @Query("SELECT o FROM Order o WHERE o.subscription.language =:language ORDER BY o.id DESC")
    List<Order> getAllByLanguage(String language);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.title =:title")
    int existByTitle(@Param("title") String title);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.subscription.language =:language")
    int count(@Param("language") String language);


}
