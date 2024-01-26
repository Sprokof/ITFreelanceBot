package telegramBot.repository.datajpa;

import telegramBot.entity.Order;
import org.springframework.stereotype.Component;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataJpaOrderRepository implements OrderRepository {

    private OrderCrudRepository crudRepository;


    @Override
    public Order save(Order order) {
        return this.crudRepository.save(order);
    }

    @Override
    public boolean exist(String link) {
        return this.crudRepository.exist(link) != 0;
    }

    @Override
    public Order getByLink(String link) {
        return this.crudRepository.getByLink(link);
    }

    @Override
    public boolean deleteByExchangeAndDate(Exchange exchange, LocalDate date) {
        return this.crudRepository.deleteByExchangeAndDate(exchange.getName(), date) != 0;
    }

    @Override
    public boolean delete(long id) {
        return this.crudRepository.delete(id) != 0;
    }

    @Override
    public List<Order> getAllByLanguage(Language language) {
        return this.crudRepository.getAllByLanguage(language.getName());
    }
}
