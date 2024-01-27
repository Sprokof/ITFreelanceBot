package telegramBot.repository.datajpa;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import telegramBot.entity.BotStatus;
import telegramBot.repository.BotStatusRepository;

@Repository
@Transactional
public class DataJpaBotStatusRepository implements BotStatusRepository {

    private final BotStatusCrudRepository crudRepository;

    public DataJpaBotStatusRepository(BotStatusCrudRepository crudRepository){
        this.crudRepository = crudRepository;
    }

    @Override
    public BotStatus get() {
        return crudRepository.get();
    }

    @Override
    public void setStatus(telegramBot.enums.BotStatus status) {
        this.crudRepository.setBotStatus(status.name());
    }
}
