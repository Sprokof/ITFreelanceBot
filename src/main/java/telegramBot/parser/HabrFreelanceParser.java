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
public class HabrFreelanceParser {
    private static final String SELECTOR = ".task__column_desc";
    public List<Order> getOrders(String link, Language language) {
        List<Order> orders = new ArrayList<>();
        Elements elements = SSLHelper.getDocument(link).select(SELECTOR);
        for (Element e : elements) {
            Element titleElement = e.child(0).child(0).child(0);
            String taskTitle = titleElement.text();
            String taskLink = titleElement.attr("href");
            String taskTags = extractTags(e);

            OrderDto dto = new OrderDto(taskTitle, taskLink, taskTags);
            if (language == Language.JAVA && OrderQueryRelationHelper.falseJavaPattern(dto)) {
                continue;
            }
            if (OrderQueryRelationHelper.correctRelation(dto, language) == language) {
                orders.add(dto.toEntity(false));
            }
        }
        return orders;
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
}
