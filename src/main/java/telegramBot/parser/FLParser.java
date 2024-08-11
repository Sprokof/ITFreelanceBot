package telegramBot.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Language;
import telegramBot.parser.helper.OrderQueryRelationHelper;
import telegramBot.parser.helper.SSLHelper;

import java.util.ArrayList;
import java.util.List;

@Component
public class FLParser {
    private static final String SELECTOR = ".search-item-body";
    public List<Order> getOrders(String link, Language language) {
        List<Order> orders = new ArrayList<>();
        Document document = SSLHelper.getDocument(link);
        Elements elements = document.select(SELECTOR);
        for (Element e : elements) {
            String taskTitle = removeEmTag(e.child(1).child(0).text());
            String taskLink = e.child(1).child(0).attr("href");
            String taskDescription = removeEmTag(e.child(2).text());

            OrderDto dto = new OrderDto(taskTitle, taskLink, taskDescription);
            if (language == Language.JAVA && OrderQueryRelationHelper.falseJavaPattern(dto)) {
                continue;
            }
            if (OrderQueryRelationHelper.correctRelation(dto, language) == language) {
                orders.add(dto.toEntity(false));
            }
        }
        return orders;
    }

    private String removeEmTag(String html) {
        return html.replaceAll("(<em>)", "").
                replaceAll("(</em>)", "");
    }
}
