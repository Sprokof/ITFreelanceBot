package telegramBot.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.enums.BotStatus;
import telegramBot.repository.BotStatusRepository;
import telegramBot.repository.datajpa.BotStatusCrudRepository;

@Component
public class DataJpaBotStatusRepository implements BotStatusRepository {

    @Autowired
    private BotStatusCrudRepository crudRepository;

    @Override
    public String getStatus() {
        return crudRepository.getStatus();
    }

    @Override
    public void setStatus(BotStatus status) {
        this.crudRepository.setBotStatus(status.name());
    }
}
