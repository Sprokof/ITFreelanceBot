package telegramBot.task;


import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.HttpMethod;
import telegramBot.enums.Language;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

public interface Task {
    Map<Exchange, List<Order>> getOrders(Language language);
    Document getDocument(String link);
    String getJSON(String link, HttpMethod httpMethod);


}
