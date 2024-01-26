package telegramBot.repository;

import org.springframework.data.repository.query.Param;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;

import java.util.List;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Subscription getByLanguage(Language language);
    boolean existByLanguageAndChatId(Language language, String chatId);
    List<Subscription> getAll();
    boolean exists(long userId);
    boolean delete(long id, long userId);
}
