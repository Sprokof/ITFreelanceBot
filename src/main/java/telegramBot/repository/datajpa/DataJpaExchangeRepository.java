package telegramBot.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.entity.Exchange;
import org.springframework.stereotype.Component;
import telegramBot.repository.ExchangeRepository;

@Component
public class DataJpaExchangeRepository implements ExchangeRepository {

    @Autowired
    private ExchangeCrudRepository crudRepository;

    @Override
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
