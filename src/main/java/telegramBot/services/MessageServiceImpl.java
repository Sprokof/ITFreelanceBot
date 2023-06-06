package telegramBot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import telegramBot.dto.OrderDto;
import telegramBot.bot.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class MessageServiceImpl implements MessageService {

    private final TelegramBot bot;
    public MessageServiceImpl(@Lazy TelegramBot bot){
        this.bot = bot;
    }

    @Override
    public void sendResponse(String userChatId, String message) {
        executeMessage(userChatId, message);
    }

    @Override
    public void sendNotice(String userChatId, Set<OrderDto> dtos) {
        executeMessage(userChatId, createNotices(dtos));
    }


    private String createNotices(Set<OrderDto> orderDtos) {
        StringBuilder notice = new StringBuilder();
        Map<Exchange, Set<OrderDto>> exchangesDtos = sortByExchange(orderDtos);
        List<StringBuilder> notices = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        for(Exchange exchange : Exchange.getExchanges()){
            if(!exchangesDtos.containsKey(exchange)){ continue; }
            Set<OrderDto> eDtos = exchangesDtos.get(exchange);
            String title = "Новые заказы на  " + exchange.getName() + ":";
            notice.append(title);
            Map<Language, Set<OrderDto>> languagesDtos = sortByLanguage(eDtos);
            for(Language language : Language.getLanguages()){
                if(!languagesDtos.containsKey(language)){ continue; }
                    Set<OrderDto> lDtos = languagesDtos.get(language);
                    String subscription = "По запросу  " + language.getName() + ":";
                    notice.append("\n").append(subscription);
                    for(OrderDto orderDto : lDtos){
                        String orderInfo = orderDto.getOrderInfo();
                        notice.append("\n").append(orderInfo);
                        notice.append(delim());
                    }
            }
        }
        notices.add(notice);

        for(StringBuilder n : notices){
            output.append(n).append("\n");
        }

    return output.toString();

    }

    private Map<Exchange, Set<OrderDto>> sortByExchange(Set<OrderDto> orderDtos){
        Map<Exchange, Set<OrderDto>> result = new HashMap<>();
        for(Exchange e : Exchange.getExchanges()){
            Set<OrderDto> dtos = orderDtos.stream().filter((o) ->
                    o.getExchangeName().equals(e.getName())).collect(Collectors.toSet());

            if(!dtos.isEmpty()) result.put(e, dtos);
        }

        return result;
    }

    private Map<Language, Set<OrderDto>> sortByLanguage(Set<OrderDto> orderDtos){
        Map<Language, Set<OrderDto>> result = new HashMap<>();
        for(Language l : Language.getLanguages()){
            Set<OrderDto> dtos = orderDtos.stream().filter((o) ->
                    o.getSubscription().equals(l.getName())).collect(Collectors.toSet());

            if(!dtos.isEmpty()) result.put(l, dtos);
        }
        return result;
    }


    public static String delim() {
        String delim = "\n.";
        for(int i = 0; i < 70; i ++){
            delim += ".";
        }
    return delim + "\n";
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
            e.printStackTrace();
        }
    }

}
