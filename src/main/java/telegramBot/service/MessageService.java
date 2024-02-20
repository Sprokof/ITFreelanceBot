package telegramBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.dto.OrderDto;
import telegramBot.bot.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repository.OrderRepository;
import telegramBot.util.BotUtil;
import telegramBot.util.OrderUtil;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MessageService {

    private final TelegramBot bot;

    public MessageService(@Lazy TelegramBot bot){
        this.bot = bot;
    }

    public void sendResponse(String userChatId, String message) {
        executeMessage(userChatId, message);
    }

    public void sendNotice(String userChatId, List<OrderDto> dtos) {
        executeMessage(userChatId, createNotices(dtos));
    }


    private String createNotices(List<OrderDto> orderDtos) {
        StringBuilder notice = new StringBuilder();
        Map<Exchange, List<OrderDto>> exchangesDtos = sortByExchange(orderDtos);
        List<StringBuilder> notices = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        for(Exchange exchange : Exchange.getExchanges()){
            if(!exchangesDtos.containsKey(exchange)){ continue; }
            List<OrderDto> eDtos = exchangesDtos.get(exchange);
            String title = "Новые заказы на  " + exchange.getName() + ":";
            notice.append(title).append("\n");
            Map<Language, List<OrderDto>> languagesDtos = sortByLanguage(eDtos);
            for(Language language : Language.getLanguages()){
                if(!languagesDtos.containsKey(language)){ continue; }
                    List<OrderDto> lDtos = languagesDtos.get(language);
                    String subscription = "По запросу  " + language.getName() + ":";
                    notice.append("\n").append(subscription).append("\n").append(delim());
                    for(OrderDto orderDto : lDtos){
                        String orderInfo = orderDto.getOrderInfo();
                        notice.append("\n")
                                .append(orderInfo)
                                .append("\n")
                                .append(delim())
                                .append("\n");
                    }
            }
        }
        notices.add(notice);

        for(StringBuilder n : notices){
            output.append(n).append("\n");
        }

    return output.toString();

    }

    private Map<Exchange, List<OrderDto>> sortByExchange(List<OrderDto> orderDtos){
        Map<Exchange, List<OrderDto>> result = new HashMap<>();
        for(Exchange e : Exchange.getExchanges()){
            List<OrderDto> dtos = orderDtos.stream()
                    .filter((o) -> o.getExchangeName().equals(e.getName()))
                    .collect(Collectors.toList());

            if(!dtos.isEmpty()) {
                result.put(e, dtos);
            }
        }

        return result;
    }

    private Map<Language, List<OrderDto>> sortByLanguage(List<OrderDto> orderDtos){
        Map<Language, List<OrderDto>> result = new HashMap<>();
        for(Language l : Language.getLanguages()){
            List<OrderDto> dtos = orderDtos.stream()
                    .filter((o) -> o.getSubscription().equals(l.getName()))
                    .collect(Collectors.toList());

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
            System.out.println(e.getCause().getMessage());
        }
    }

    public String getLatestOrdersMessage(Update update, OrderService service) {
        String input = update.getMessage().getText().
                replaceAll("\\s", "");
        String[] items = extractItems(input);
        Language language = Language.ignoreCaseValueOf(items[0]);
        int count = Integer.parseInt(items[1]);
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
            sb.append(o.getOrderInfo()).append(" (").
                    append(o.getExchangeName()).
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
        String count = (items[1].
                matches("\\d+") &&
                !items[1].equals("0")) ? items[1] : "1" ;
        return new String[]{language, count};
    }

}
