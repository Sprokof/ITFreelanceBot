package telegramBot.repository.datajpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telegramBot.entity.BotStatus;

public interface BotStatusCrudRepository extends CrudRepository<BotStatus, Long> {
    @Query("SELECT s FROM BotStatus s")
    String getStatus();

    @Modifying
    @Query("UPDATE BotStatus s SET s.status =:status")
    void setBotStatus(@Param("status") String status);
}
