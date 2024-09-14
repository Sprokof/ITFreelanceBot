package telegramBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.dto.OrderDto;
import telegramBot.bot.TelegramBot;
import org.springframework.context.annotation.Lazy;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.service.OrderService;
import telegramBot.util.OrderUtil;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static telegramBot.util.BotUtil.MAX_ORDERS_COUNT;


@Service
public final class MessageService {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private final TelegramBot bot;

    public MessageService(@Lazy TelegramBot bot){
        this.bot = bot;
    }

    public void sendResponse(String userChatId, String message) {
        executeMessage(userChatId, message);
    }

    public void sendNotice(String userChatId, Set<OrderDto> dtos) {
        executeMessage(userChatId, createNotices(dtos));
    }


    private String createNotices(Set<OrderDto> orderDtos) {
        orderDtos = OrderUtil.extractMaxOrdersCount(orderDtos);
        Map<Exchange, Set<OrderDto>> exchangesDtos = sortByExchange(orderDtos);
        StringBuilder result = new StringBuilder();
        for(Exchange exchange : Exchange.get()){
            StringBuilder notice = new StringBuilder();
            if(!exchangesDtos.containsKey(exchange)){ continue; }
            Set<OrderDto> eDtos = exchangesDtos.get(exchange);
            String title = "Новые заказы на  " + exchange.getName() + ":";
            notice.append(title).append("\n");
            Map<Language, Set<OrderDto>> languagesDtos = sortByLanguage(eDtos);
            for(Language language : Language.getLanguages()){
                if(!languagesDtos.containsKey(language)){ continue; }
                    Set<OrderDto> lDtos = languagesDtos.get(language);
                    String subscription = "По запросу  " + language.getName() + ":";
                    notice.append("\n").append(subscription).append("\n").append(delim());
                    for(OrderDto orderDto : lDtos) {
                        String info = OrderUtil.buildNotice(orderDto);
                        notice.append("\n")
                                .append(info)
                                .append("\n")
                                .append(delim())
                                .append("\n");
                    }
            }
            result.append(notice).append("\n");
        }
    return result.toString();

    }

    private Map<Exchange, Set<OrderDto>> sortByExchange(Set<OrderDto> orderDtos){
        Map<Exchange, Set<OrderDto>> result = new HashMap<>();
        for (Exchange e : Exchange.get()) {
            Set<OrderDto> dtos = orderDtos.stream()
                    .filter((o) -> o.getExchange().getName().equals(e.getName()))
                    .collect(Collectors.toSet());

            if (!dtos.isEmpty()) {
                result.put(e, dtos);
            }
        }
        return result;
    }

    private Map<Language, Set<OrderDto>> sortByLanguage(Set<OrderDto> orderDtos){
        Map<Language, Set<OrderDto>> result = new HashMap<>();
        for (Language l : Language.getLanguages()){
            Set<OrderDto> dtos = orderDtos.stream()
                    .filter((o) -> o.getSubscription().getLanguage().equals(l.getName()))
                    .collect(Collectors.toSet());

            if(!dtos.isEmpty()) {
                result.put(l, dtos);
            }
        }
        return result;
    }


    public static String delim() {
        return ".".repeat(70);
    }

    private String trimImgTag(String message){
        return message.replaceAll("(<img>)", "");
    }

    private SendMessage buildMessage(String chatId, String content){
        org.telegram.telegrambots.meta.api.methods.send.SendMessage sendMessage =
                new org.telegram.telegrambots.meta.api.methods.send.SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(trimImgTag(content));
        sendMessage.disableWebPagePreview();
    return sendMessage;
    }


    private void executeMessage(String userChatId, String message){
        try {
            bot.execute(buildMessage(userChatId, message));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
    }

    public String getLatestOrdersMessage(Update update, OrderService service) {
        String input = update.getMessage().getText().
                replaceAll("\\s", "");
        String[] items = extractItems(input);
        Language language = Language.ignoreCaseValueOf(items[0]);
        int count = Math.min(Integer.parseInt(items[1]), MAX_ORDERS_COUNT);
        List<Order> orders = service.getAllByLanguage(language);
        List<OrderDto> dtos = OrderUtil.toDtos(orders).subList(0,
                getEndIndex(orders.size(), count));
        assert language != null;
        return createMessage(language, dtos);
    }

    private String createMessage(Language language, List<OrderDto> dtos){
        StringBuilder sb = new StringBuilder("Последние заказы по запросу " +
                language.getName() + ":\n" );
        dtos.forEach(o -> {
            sb.append(OrderUtil.buildNotice(o)).append(" (").
                    append(o.getExchange().getName()).
                    append(")").append("\n\n");
        });

        return sb.toString();
    }

    private int getEndIndex(int collectionSize, int count){
        return Math.min(collectionSize, count);
    }

    private String[] extractItems(String input){
        String[] items = input.split(",");
        String language = items[0];
        String count = (items[1].matches("\\d+") &&
                !items[1].equals("0")) ? items[1] : "1" ;
        return new String[]{language, count};
    }
}
