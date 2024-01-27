package telegramBot.repository.datajpa;

import org.springframework.stereotype.Repository;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.repository.SubscriptionRepository;

import jakarta.transaction.Transactional;
import java.util.List;


@Repository
public class DataJpaSubscriptionRepository implements SubscriptionRepository {

    private final SubscriptionCrudRepository crudRepository;

    public DataJpaSubscriptionRepository(SubscriptionCrudRepository crudRepository){
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
    public Subscription save(Subscription subscription) {
        return this.crudRepository.save(subscription);
    }

    @Override
    public Subscription getByLanguage(Language language) {
        return this.crudRepository.getByLanguage(language.getName());
    }

    @Override
    public List<Subscription> getAll() {
        return (List<Subscription>) this.crudRepository.findAll();
    }

}
