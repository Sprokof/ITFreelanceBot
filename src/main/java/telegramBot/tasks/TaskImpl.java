package telegramBot.tasks;


import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.HttpMethod;
import telegramBot.enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TaskImpl implements Task {
    private static final String HABR_SELECTOR = ".task__title a";
    private static final String FL_SELECTOR = ".search-item-body h3 a";
    private static final Map<String, String> habrLinks = new HashMap<>();
    private static final Map<String, String> flLinks = new HashMap<>();
    private static final Map<String, String> kworkLinks = new HashMap<>();


    static {
        habrLinks.put(Language.JAVA.getName(), "https://freelance.habr.com/tasks?page=1&q=java&fields=tags,title,description");
        habrLinks.put(Language.PYTHON.getName(), "https://freelance.habr.com/tasks?page=1&q=python&fields=tags,title,description");
        habrLinks.put(Language.JAVASCRIPT.getName(), "https://freelance.habr.com/tasks?page=1&q=javascript&fields=tags,title,description");
        habrLinks.put(Language.PHP.getName(), "https://freelance.habr.com/tasks?page=1&q=phpfields=tags,title,description");

        flLinks.put(Language.JAVA.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=java&page=1");
        flLinks.put(Language.PYTHON.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=python&page=1");
        flLinks.put(Language.JAVASCRIPT.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=javascript&page=1");
        flLinks.put(Language.PHP.getName(),
                "https://www.fl.ru/search/?action=search&type=projects&search_string=php&page=1");

        kworkLinks.put(Language.JAVA.getName(), "https://kwork.ru/projects?keyword=java&a=1.json");
        kworkLinks.put(Language.PYTHON.getName(), "https://kwork.ru/projects?keyword=python&a=1.json");
        kworkLinks.put(Language.JAVASCRIPT.getName(), "https://kwork.ru/projects?keyword=javacscript&a=1.json");
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
        String link = habrLinks.get(language.getName());
        Document document = getDocument(link);
        Elements elements = document.select(HABR_SELECTOR);
        List<Order> orders = new ArrayList<>();
        for (Element e : elements) {
            String taskTitle = e.text();
            String taskLink = e.attr("href");

            orders.add(new Order(taskTitle, taskLink));

        }
        return orders;
    }

    private List<Order> getFlOrders(Language language) {
        String link = flLinks.get(language.getName());
        Document document = getDocument(link);
        Elements elements = document.select(FL_SELECTOR);
        List<Order> orders = new ArrayList<>();
        for (Element e : elements) {
            String taskTitle = trimHtml(e.html());
            String taskLink = e.attr("href");

            orders.add(new Order(taskTitle, taskLink));

        }

        return orders;
    }

    private List<Order> getKworkOrders(Language language) {
        String link = kworkLinks.get(language.getName());
        String kworkJson = getJSON(link, HttpMethod.POST);
        return extractKworkOrders(kworkJson);
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
    
    private List<Order> extractKworkOrders(String json){
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

    private Order mapToKworkOrder(String json){
        String idPrefix = "\"id\"", namePrefix = "\"name\"";
        String title = null, link = null;
        String[] fields = json.split("(,\"|\",)");
        for (String field : fields) {
            if (title != null && link != null) break;

            if (field.startsWith(idPrefix)) {
                int index = field.indexOf(":") + 1;
                link = "/projects/" + field.substring(index);
            }

            if (field.startsWith(namePrefix)) {
                int index = field.indexOf(":") + 1;
                title = field.substring(index).replaceAll("\"", "");
            }
        }
    return new Order(title, link);
    }



}
