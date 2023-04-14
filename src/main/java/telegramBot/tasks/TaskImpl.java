package telegramBot.tasks;

import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TaskImpl implements Task {
    private static final String HABR_SELECTOR = ".task__title a";
    private static final String FL_SELECTOR = ".search-item-body h3 a";

    private static ParseClass flClasses;
    private static final Map<String, String> habrLinks = new HashMap<>();

    private static final Map<String, String> flLinks = new HashMap<>();


    static {
        habrLinks.put(Language.JAVA.getName(), "https://freelance.habr.com/tasks?page=1&q=java");
        habrLinks.put(Language.PYTHON.getName(), "https://freelance.habr.com/tasks?page=1&q=python");
        habrLinks.put(Language.JAVASCRIPT.getName(), "https://freelance.habr.com/tasks?page=1&q=javascript");

        flLinks.put(Language.JAVA.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=java&page=1");
        flLinks.put(Language.PYTHON.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=python&page=1");
        flLinks.put(Language.JAVASCRIPT.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=javascript&page=1");

        //habrClasses = new ParseClass("task_title", "task_title a");
        //flClasses = new ParseClass(".select-item-body a");
    }

    @Override
    public Map<Exchange, List<Order>> getOrders(Language language) {
        Map<Exchange, List<Order>> exchangeOrders = new HashMap<>();
        exchangeOrders.put(Exchange.HABR_FREELANCE, getHabrOrders(language));
        exchangeOrders.put(Exchange.FL_RU, getFlOrders(language));
    return exchangeOrders;

    }

    private List<Order> getHabrOrders(Language language){
        String link = habrLinks.get(language.getName());
        Document document = getDocument(link);
        Elements elements = document.select(HABR_SELECTOR);
        List<Order> orders = new ArrayList<>();
        for(Element e : elements){
            String taskTitle = e.text();
            String taskLink = e.attr("href");

            orders.add(new Order(taskTitle, taskLink));

        }
        return orders;
    }

    private List<Order> getFlOrders(Language language){
        String link = flLinks.get(language.getName());
        Document document = getDocument(link);
        Elements elements = document.select(FL_SELECTOR);
        List<Order> orders = new ArrayList<>();
        for(Element e : elements){
            String taskTitle = trimHtml(e.html());
            String taskLink = e.attr("href");

            orders.add(new Order(taskTitle, taskLink));

        }

        return orders;
    }

    @Override
    public Document getDocument(String link) {
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private String trimHtml(String html){
        return html.replaceAll("(<em>)", "").
                replaceAll("(</em>)", "");
    }

}
