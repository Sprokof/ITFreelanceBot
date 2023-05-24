package telegramBot.tasks;


import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.HttpMethod;
import telegramBot.enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import telegramBot.services.InitStatusService;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TaskImpl implements Task {
    private static final String HABR_SELECTOR = ".task__column_desc";
    private static final String FL_SELECTOR = ".search-item-body";
    private static final Map<String, String> habrLinks = new HashMap<>();
    private static final Map<String, String> flLinks = new HashMap<>();
    private static final Map<String, String> kworkLinks = new HashMap<>();


    static {
        habrLinks.put(Language.JAVA.getName(), "https://freelance.habr.com/tasks?page=1&q=java&fields=tags");
        habrLinks.put(Language.PYTHON.getName(), "https://freelance.habr.com/tasks?page=1&q=python&fields=tags");
        habrLinks.put(Language.JAVASCRIPT.getName(), "https://freelance.habr.com/tasks?page=1&q=javascript&fields=tags|" +
                "https://freelance.habr.com/tasks?page=1&q=java%20script&fields=tags|" +
                "https://freelance.habr.com/tasks?page=1&q=js&fields=tags");
        habrLinks.put(Language.PHP.getName(), "https://freelance.habr.com/tasks?page=1&q=php&fields=tags");

        flLinks.put(Language.JAVA.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=java&page=1");
        flLinks.put(Language.PYTHON.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=python&page=1");
        flLinks.put(Language.JAVASCRIPT.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=javascript&page=1|" +
                "https://www.fl.ru/search/?action=search&type=projects&search_string=java%20script&page=1|" +
                        "https://www.fl.ru/search/?action=search&type=projects&search_string=js&page=1");
        flLinks.put(Language.PHP.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=php&page=1");

        kworkLinks.put(Language.JAVA.getName(), "https://kwork.ru/projects?keyword=java&a=1.json");
        kworkLinks.put(Language.PYTHON.getName(), "https://kwork.ru/projects?keyword=python&a=1.json");
        kworkLinks.put(Language.JAVASCRIPT.getName(), "https://kwork.ru/projects?keyword=javascript&a=1.json|" +
                "https://kwork.ru/projects?keyword=java+script&a=1.json|" +
                "https://kwork.ru/projects?keyword=js&a=1.json");
        kworkLinks.put(Language.PHP.getName(), "https://kwork.ru/projects?keyword=php&a=1.json");

    }


    @Override
    public Map<Exchange, List<Order>> getOrders(Language language) {
        Map<Exchange, List<Order>> exchangeOrders = new HashMap<>();
        exchangeOrders.put(Exchange.HABR_FREELANCE, getHabrOrders(language));
        exchangeOrders.put(Exchange.FL_RU, getFlOrders(language));
        exchangeOrders.put(Exchange.KWORK, getKworkOrders(language));

        return exchangeOrders;

    }

    private List<Order> getHabrOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for(String link : habrLinks.get(language.getName()).split("\\|")) {
            Document document = getDocument(link);
            Elements elements = document.select(HABR_SELECTOR);
            for (Element e : elements) {
                Element titleElement = e.child(0).child(0).child(0);
                String taskTitle = titleElement.text();
                String taskLink = titleElement.attr("href");
                String taskDate = e.child(0).child(1).child(1).child(0).text();
                String taskTags = extractTags(e);
                
                OrderDto dto = new OrderDto(taskTitle, taskLink, taskTags);
                if(OrderQueryRelation.correctRelation(dto, language).equals(language)){
                    Order order = dto.doBuild();
                    if(InitStatusService.init() && newOrder(taskDate)) orders.add(order);
                    else if(!InitStatusService.init()) orders.add(order);
                }
            }
        }

        return orders;
    }

    private List<Order> getFlOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : flLinks.get(language.getName()).split("\\|")) {
            Document document = getDocument(link);
            Elements elements = document.select(FL_SELECTOR);
            for (Element e : elements) {
                String taskTitle = trimHtml(e.child(1).child(0).text());
                String taskLink = e.child(1).child(0).attr("href");
                String taskDescription = trimHtml(e.child(2).text());

                OrderDto dto = new OrderDto(taskTitle, taskLink, taskDescription);
                if (OrderQueryRelation.correctRelation(dto, language).equals(language)) {
                    orders.add(dto.doBuild());
                }
            }
        }
        return orders;
    }

    private List<Order> getKworkOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : kworkLinks.get(language.getName()).split("\\|")) {
            String kworkJson = getJSON(link, HttpMethod.POST);
            List<Order> filteredOrders = extractKworkOrders(kworkJson).stream().filter(order ->
                    OrderQueryRelation.correctRelation(order, language).equals(language)).
                    map(OrderDto::doBuild).collect(Collectors.toList());
            orders.addAll(filteredOrders);
        }
    return orders;
    }

    @Override
    public Document getDocument(String link) {
        Document document = null;
        try {
            document = SSLHelper.getConnection(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private String trimHtml(String html) {
        return html.replaceAll("(<em>)", "").
                replaceAll("(</em>)", "");
    }

    @Override
    public String getJSON(String link, HttpMethod httpMethod) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(link);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(httpMethod.getMethodName());
            c.setRequestProperty("Content-length", "0");
            c.setRequestProperty("Content-Type", "application/json");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return null;
    }

    private List<OrderDto> extractKworkOrders(String json){
        return Arrays.stream(json.
                split("(\\{|\\})")).
                filter(this::filterCondition).
                map(StringEscapeUtils :: unescapeJava).
                map(this :: mapToKworkOrder).
                collect(Collectors.toList());
    }

    private boolean filterCondition(String obj) {
        String idPat = "(\"id\")(:)\\d{7}";
        String langPat = "(\"lang\")(:)" + "\"" +"[a-z]{2}"+ "\"";
        int index = obj.indexOf(",");
        if(index != -1 && obj.substring(0, index).
                matches(idPat)){
            return obj.split(",")[1].matches(langPat);
        }
        return false;
    }

    private OrderDto mapToKworkOrder(String json){
        String idPrefix = "\"id\"", namePrefix = "\"name\"", descPrefix = "\"description\"";
        String title = null, link = null, description = null;
        String[] fields = json.split("(,\"|\",)");
        int index = 0;
        while(index != fields.length){
            String field = fields[index];
            if(link != null && title != null && description != null) break;

            if (field.startsWith(idPrefix)) {
                link = "/projects/" + field.substring(field.indexOf(":") + 1);
            }

            if (field.startsWith(namePrefix)) {
                title = field.substring(field.indexOf(":") + 1).
                        replaceAll("\"", "").trim();
            }

            if(field.startsWith(descPrefix)){
                int subIndex = field.indexOf(":") + 1;
                description = field.substring(subIndex).
                        replaceAll("\"", "").trim();
            }
            index ++ ;

        }


    return new OrderDto(title, link, description);
    }

    private boolean newOrder(String publishedDate){
        String pattern = "(~)\\s\\d{1,2}\\s(часов назад|час назад)|\\d{1,2}\\s(минуты назад|минут назад)";
        return publishedDate.matches(pattern);
    }

    private String extractTags(Element element){
        Elements elements = element.child(1).child(0).children();
        StringBuilder sb = new StringBuilder();
        for(Element e : elements){
            sb.append(e.text()).append(",");
        }
    return sb.toString();
    }



}
