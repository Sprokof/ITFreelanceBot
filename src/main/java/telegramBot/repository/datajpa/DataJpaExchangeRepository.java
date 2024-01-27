package telegramBot.repository.datajpa;

import org.springframework.stereotype.Repository;
import telegramBot.entity.Exchange;
import telegramBot.repository.ExchangeRepository;

import jakarta.transaction.Transactional;

@Repository
public class DataJpaExchangeRepository implements ExchangeRepository {

    private final ExchangeCrudRepository crudRepository;

    public DataJpaExchangeRepository(ExchangeCrudRepository crudRepository){
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
    public Exchange save(Exchange exchange) {
        return this.crudRepository.save(exchange);
    }

    @Override
    public boolean existByName(String name) {
        return this.crudRepository.existByName(name) != 0;
    }

    @Override
    public Exchange getByName(String name) {
        return crudRepository.getByName(name);
    }
}
