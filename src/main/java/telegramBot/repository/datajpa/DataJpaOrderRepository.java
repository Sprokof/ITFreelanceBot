package telegramBot.repository.datajpa;

import org.springframework.stereotype.Repository;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repository.OrderRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class DataJpaOrderRepository implements OrderRepository {

    private final OrderCrudRepository crudRepository;

    public DataJpaOrderRepository(OrderCrudRepository crudRepository){
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return this.crudRepository.save(order);
    }

    @Override
    public boolean existByLink(String link) {
        return this.crudRepository.existByLink(link) != 0;
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

    @Override
    public boolean existByTitle(String title) {
        return this.crudRepository.existByTitle(title) != 0;
    }
}
