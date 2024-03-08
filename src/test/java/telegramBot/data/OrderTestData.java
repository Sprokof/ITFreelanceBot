package telegramBot.data;

import telegramBot.entity.Order;
import telegramBot.matcher.MatcherFactory;

public class OrderTestData {
    public static final MatcherFactory.Matcher<Order> ORDER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Order.class,"exchange", "subscription");
    public static Order ORDER = new Order("orderTitle", "orderLink");
    public static final long CREATED_ORDER_ID = 4;

    public static Order getNew() {
        return new Order("title", "link");
    }

    public static Order updated(){
        Order updated = new Order(ORDER);
        updated.setTitle("updatedTitle");
        updated.setLink("updatedLink");
        return updated;
    }

}
