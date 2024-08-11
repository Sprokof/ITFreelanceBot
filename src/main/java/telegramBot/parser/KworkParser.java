package telegramBot.parser;

import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Language;
import telegramBot.parser.helper.OrderQueryRelationHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class KworkParser {
    private static final String SPLIT_PATTERN = "\"userAlreadyWork\":null";
    private static final Pattern SKIP_PATTERN = Pattern.compile("\"success\":true");
    private static final Logger LOGGER = Logger.getLogger(KworkParser.class.getSimpleName());

    private static String convertToCyrillic(String input) {
        Pattern unicodeCyrillicPattern = Pattern.compile("4(\\d{2}|(\\d\\w))");
        String splitPattern = "(\\\\u0|\\\\\"u0)";
        String prefix = "0";
        String[] inputItems = input.split(splitPattern);
        StringBuilder result = new StringBuilder(inputItems[0]);
        for (int i = 1; i < inputItems.length; i ++) {
            String item = inputItems[i];
            if (unicodeCyrillicPattern.matcher(item).find()) {
                String hex = (prefix + item.substring(0, 3));
                String trimPart = "";
                if (item.length() > 3) {
                    trimPart = item.substring(3);
                }
                int asciiCode = Integer.parseInt(hex, 16);
                char asciiChar = (char) asciiCode;
                result.append(asciiChar).append(trimPart);
            } else {
                result.append(item);
            }
        }
        return result.toString();
    }

    public List<Order> getOrders(String link, Language language) {
        return this.extractOrders(link).stream()
                .filter(order -> {
                    if (language.equals(Language.JAVA)) {
                        return !OrderQueryRelationHelper.falseJavaPattern(order) &&
                                OrderQueryRelationHelper.correctRelation(order, language) == language;
                    }
                    return OrderQueryRelationHelper.correctRelation(order, language) == language;
                })
                .map(orderDto -> orderDto.toEntity(false))
                .toList();
    }

    private List<OrderDto> extractOrders(String link) {
        String json = getJson(link);
        if (json.isEmpty()) return new ArrayList<>();

        String convertedJson = KworkParser.convertToCyrillic(json
                .replaceAll("(\\{|\\})", ""));
        String[] jsonArray = convertedJson.split(KworkParser.SPLIT_PATTERN);


        return Arrays.stream(jsonArray)
                .filter(this::filterCondition)
                .map(this::mapToKworkOrder)
                .collect(Collectors.toList());
    }

    private boolean filterCondition(String record) {
        return !KworkParser.SKIP_PATTERN.matcher(record).find();
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

    private String getJson(String link) {
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
        } catch (InterruptedException | IOException e) {
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
        return responseBody;
    }
}
