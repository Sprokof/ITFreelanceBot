package telegramBot.task;


import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import telegramBot.util.BotUtil;
import telegramBot.util.KworkUtil;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static telegramBot.util.KworkUtil.SKIP_PATTERN;


@Component
public class ExchangeParser {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private static final String HABR_SELECTOR = ".task__column_desc";
    private static final String FL_SELECTOR = ".search-item-body";
    private static final Map<String, String> habrLinks = new HashMap<>();
    private static final Map<String, String> flLinks = new HashMap<>();
    private static final Map<String, String> kworkLinks = new HashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(BotUtil.COUNT_EXCHANGES);
    private Future<List<Order>> habrOrders;
    private Future<List<Order>> flOrders;
    private Future<List<Order>> kworkOrders;

    static {

        habrLinks.put(Language.JAVA.getName(), habrLik(Language.JAVA));
        habrLinks.put(Language.PYTHON.getName(), habrLik(Language.PYTHON));
        habrLinks.put(Language.JAVASCRIPT.getName(), habrJavaScriptLink());
        habrLinks.put(Language.PHP.getName(), habrLik(Language.PHP));
        habrLinks.put(Language.C.getName(), habrLik(Language.C));
        habrLinks.put(Language.RUBY.getName(), habrLik(Language.RUBY));

        flLinks.put(Language.JAVA.getName(), flLink(Language.JAVA));
        flLinks.put(Language.PYTHON.getName(), flLink(Language.PYTHON));
        flLinks.put(Language.JAVASCRIPT.getName(), flJavaScriptLink());
        flLinks.put(Language.PHP.getName(), flLink(Language.PHP));
        flLinks.put(Language.C.getName(), flLink(Language.C));
        flLinks.put(Language.RUBY.getName(), flLink(Language.RUBY));

        kworkLinks.put(Language.JAVA.getName(), kworkLink(Language.JAVA));
        kworkLinks.put(Language.PYTHON.getName(), kworkLink(Language.PYTHON));
        kworkLinks.put(Language.JAVASCRIPT.getName(), kworkJavaScriptLink());
        kworkLinks.put(Language.PHP.getName(), kworkLink(Language.PHP));
        kworkLinks.put(Language.C.getName(), kworkLink(Language.C));
        kworkLinks.put(Language.RUBY.getName(), kworkLink(Language.RUBY));
        kworkLinks.put(Language.PHP.getName(), kworkLink(Language.PHP));

    }


    public Map<Exchange, List<Order>> get() {
        Map<Exchange, List<Order>> exchangesOrders = new ConcurrentHashMap<>();
        try {
            exchangesOrders.put(Exchange.HABR_FREELANCE, habrOrders.get());
            exchangesOrders.put(Exchange.FL_RU, flOrders.get());
            exchangesOrders.put(Exchange.KWORK, kworkOrders.get());
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return exchangesOrders;
    }

    public ExchangeParser findOrders(Language language) {
        this.habrOrders = executorService.submit(() -> getHabrOrders(language));
        this.flOrders = executorService.submit(() -> getFlOrders(language));
        this.kworkOrders = executorService.submit(() -> getKworkOrders(language));
        return this;
    }

    private boolean isDone() {
        return habrOrders.isDone() && flOrders.isDone() && kworkOrders.isDone();
    }

    public ExchangeParser waitFinish() {
        while (!isDone()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
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
                String taskTags = extractTags(e);

                OrderDto dto = new OrderDto(taskTitle, taskLink, taskTags);
                if (language == Language.JAVA && OrderQueryRelation.falseJavaPattern(dto)) {
                    continue;
                }
                if (OrderQueryRelation.correctRelation(dto, language) == language) {
                    orders.add(dto.toEntity(false));
                }
            }
        }

        return orders;
    }

    private List<Order> getFlOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : flLinks.get(language.getName()).split("\\|")) {
            Optional<Document> document = Optional.of(getDocument(link));
            if (document.isPresent()) {
                Elements elements = document.get().select(FL_SELECTOR);
                for (Element e : elements) {
                    String taskTitle = trimHtml(e.child(1).child(0).text());
                    String taskLink = e.child(1).child(0).attr("href");
                    String taskDescription = trimHtml(e.child(2).text());

                    OrderDto dto = new OrderDto(taskTitle, taskLink, taskDescription);
                    if (language == Language.JAVA && OrderQueryRelation.falseJavaPattern(dto)) {
                        continue;
                    }
                    if (OrderQueryRelation.correctRelation(dto, language) == language) {
                        orders.add(dto.toEntity(false));
                    }
                }
            }
        }
        return orders;
    }

    private List<Order> getKworkOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : kworkLinks.get(language.getName()).split("\\|")) {
            String kworkJson = getJson(link);
            List<Order> filteredOrders = extractKworkOrders(kworkJson).stream()
                    .filter(order -> {
                if (!order.containsNull() && language.equals(Language.JAVA)) {
                    return !OrderQueryRelation.falseJavaPattern(order) &&
                            OrderQueryRelation.correctRelation(order, language) == language;
                }
                return !order.containsNull() && OrderQueryRelation.correctRelation(order, language) == language;
            })
                    .map(orderDto -> orderDto.toEntity(false))
                    .collect(Collectors.toList());
            orders.addAll(filteredOrders);
        }
    return orders;
    }

    public Document getDocument(String link) {
        Document document = null;
        try {
            document = SSLHelper.getConnection(link).get();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
        return document;
    }

    private String trimHtml(String html) {
        return html.replaceAll("(<em>)", "").
                replaceAll("(</em>)", "");
    }


    public String getJson(String link) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        String responseBody = "";
    try {
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
       responseBody = response.body();
    }
    catch (InterruptedException | IOException e) {
        LOGGER.log(Level.SEVERE, "an exception was thrown", e);
    }
    return responseBody;
    }

    private List<OrderDto> extractKworkOrders(String json) {
        if (json.isEmpty()) return new ArrayList<>();

        String convertedJson = KworkUtil.convertToCyrillic(json
                .replaceAll("(\\{|\\})", ""));
        String[] jsonArray = convertedJson.split(KworkUtil.SPLIT_PATTERN);


        return Arrays.stream(jsonArray)
                .filter(this::filterCondition)
                .map(this::mapToKworkOrder)
                .collect(Collectors.toList());
    }

    private boolean filterCondition(String record) {
        return !SKIP_PATTERN.matcher(record).find();
    }

    private OrderDto mapToKworkOrder(String json) {
        String idPrefix = "id\"", descPrefix = "description\"", titlePrefix = "name\"";
        String link = null, description = null, title = null;
        String[] fields = json.split("(,\")");
        int index = 0;
        while (index != fields.length) {
            String field = fields[index];
            if (link != null && description != null && title != null) {
                break;
            }

            if (field.startsWith(idPrefix)) {
                link = "/projects/" + field.substring(field.indexOf(":") + 1);
            }

            if (field.startsWith(descPrefix)) {
                int subIndex = field.indexOf(":") + 1;
                description = field.substring(subIndex).
                        replaceAll("\"", "").trim();
            }

            if (field.startsWith(titlePrefix)) {
                title = field.substring(field.indexOf(":") + 1).
                        replaceAll("\"", "").trim();
            }

            index++;
        }
        return new OrderDto(title, link, description);
    }

    private String extractTags(Element element) {
        Elements elements = element.child(1)
                .child(0)
                .children();
        StringBuilder sb = new StringBuilder();
        for (Element e : elements) {
            sb.append(e.text()).append(",");
        }
        return sb.toString();
    }

    private static String habrLik(Language language){
        String link = "https://freelance.habr.com/tasks?page=1&q=lang&fields=tags";
        return link.replaceAll("(lang)", language.getName().toLowerCase());
    }

    private static String habrJavaScriptLink() {
        return "https://freelance.habr.com/tasks?page=1&q=javascript&fields=tags|" +
                "https://freelance.habr.com/tasks?page=1&q=java%20script&fields=tags|" +
                "https://freelance.habr.com/tasks?page=1&q=js&fields=tags";
    }

    private static String flLink(Language language) {
        String link = "https://www.fl.ru/search/?action=search&type=projects&search_string=lang&page=1";
        return link.replaceAll("(lang)", language.getName().toLowerCase());
    }

    private static String flJavaScriptLink() {
        return "https://www.fl.ru/search/?action=search&type=projects&search_string=javascript&page=1|" +
                "https://www.fl.ru/search/?action=search&type=projects&search_string=java%20script&page=1|" +
                "https://www.fl.ru/search/?action=search&type=projects&search_string=js&page=1";
    }

    private static String kworkLink(Language language) {
        String link = "https://kwork.ru/projects?keyword=lang&c=all.json";
        return link.replaceAll("(lang)", language.getName()).toLowerCase();
    }

    private static String kworkJavaScriptLink() {
        return "https://kwork.ru/projects?keyword=javascript&c=all.json|" +
                "https://kwork.ru/projects?keyword=java+script&c=all.json|" +
                "https://kwork.ru/projects?keyword=js&c=all.json";
    }
}
