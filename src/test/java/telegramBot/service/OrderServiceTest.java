package telegramBot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import telegramBot.entity.Order;
import telegramBot.h2.H2Config;

import static telegramBot.data.OrderTestData.ORDER_MATCHER;
import static telegramBot.data.OrderTestData.getNew;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {H2Config.class})
public class OrderServiceTest {

    @Autowired
    private OrderService service;

    @Test
    public void create(){
        Order created = service.create(getNew());
        long newId = created.id();
        Order newOrder = getNew();
        newOrder.setId(newId);
        ORDER_MATCHER.assertMatch(created, newOrder);
    }
}
