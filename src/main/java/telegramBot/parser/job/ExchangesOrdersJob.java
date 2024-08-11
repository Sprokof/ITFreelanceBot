package telegramBot.parser.job;


import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import org.springframework.stereotype.Component;
import telegramBot.parser.FLParser;
import telegramBot.parser.HabrFreelanceParser;
import telegramBot.parser.KworkParser;
import telegramBot.util.BotUtil;
import java.util.*;
import java.util.concurrent.*;

@Component
public class ExchangesOrdersJob {
    private static final Map<String, String> habrLinks = new HashMap<>();
    private static final Map<String, String> flLinks = new HashMap<>();
    private static final Map<String, String> kworkLinks = new HashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(BotUtil.COUNT_EXCHANGES);
    private final KworkParser kworkParser;
    private final HabrFreelanceParser habrFreelanceParser;
    private final FLParser flParser;
    private Future<List<Order>> habrOrders;
    private Future<List<Order>> flOrders;
    private Future<List<Order>> kworkOrders;

    public ExchangesOrdersJob(KworkParser kworkParser, HabrFreelanceParser habrFreelanceParser, FLParser flParser) {
        this.kworkParser = kworkParser;
        this.habrFreelanceParser = habrFreelanceParser;
        this.flParser = flParser;
    }

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

    public ExchangesOrdersJob findOrders(Language language) {
        this.habrOrders = executorService.submit(() -> getHabrOrders(language));
        this.flOrders = executorService.submit(() -> getFlOrders(language));
        this.kworkOrders = executorService.submit(() -> getKworkOrders(language));
        return this;
    }

    private boolean isDone() {
        return habrOrders.isDone() && flOrders.isDone() && kworkOrders.isDone();
    }

    public ExchangesOrdersJob waitFinish() {
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
            orders.addAll(this.habrFreelanceParser.getOrders(link, language));
        }
        return orders;
    }

    private List<Order> getFlOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : flLinks.get(language.getName()).split("\\|")) {
            orders.addAll(this.flParser.getOrders(link, language));
        }
        return orders;
    }

    private List<Order> getKworkOrders(Language language) {
        List<Order> orders = new ArrayList<>();
        for (String link : kworkLinks.get(language.getName()).split("\\|")) {
            orders.addAll(this.kworkParser.getOrders(link, language));
        }
        return orders;
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
