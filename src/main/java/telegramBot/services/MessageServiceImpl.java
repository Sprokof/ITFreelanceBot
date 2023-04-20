package telegramBot.services;

import telegramBot.dto.OrderDto;
import telegramBot.bot.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MessageServiceImpl implements MessageService {

    private final TelegramBot bot;
    public MessageServiceImpl(@Lazy TelegramBot bot){
        this.bot = bot;
    }

    @Override
    public void sendResponse(String userChatId, String message) {
        org.telegram.telegrambots.meta.api.methods.send.SendMessage sendMessage =
                new org.telegram.telegrambots.meta.api.methods.send.SendMessage();
        sendMessage.setChatId(userChatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNotice(String userChatId, List<OrderDto> dto) {
        org.telegram.telegrambots.meta.api.methods.send.SendMessage sendMessage =
                new org.telegram.telegrambots.meta.api.methods.send.SendMessage();
        sendMessage.setChatId(userChatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(createNotices(dto));
        sendMessage.disableWebPagePreview();
        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createNotices(List<OrderDto> orderDtos) {
        Map<Exchange, List<OrderDto>> exchangesDtos = sortByExchange(orderDtos);
        List<StringBuilder> notices = new ArrayList<>();
        StringBuilder notice = new StringBuilder();
        StringBuilder output = new StringBuilder();
        for(Exchange exchange : Exchange.getExchanges()){
            if(!exchangesDtos.containsKey(exchange)){ continue; }
            List<OrderDto> eDtos = exchangesDtos.get(exchange);
            String title = "Новые заказы на  " + exchange.getName() + ":";
            notice.append(title);
            Map<Language, List<OrderDto>> languagesDtos = sortByLanguage(eDtos);
            for(Language language : Language.getLanguages()){
                if(!languagesDtos.containsKey(language)){ continue; }
                    List<OrderDto> lDtos = languagesDtos.get(language);
                    String subscription = "По запросу  " + language.getName() + ":";
                    notice.append("\n").append(subscription);
                    for(OrderDto orderDto : lDtos){
                        String orderInfo = getOrderInfo(orderDto);
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

    private Map<Exchange, List<OrderDto>> sortByExchange(List<OrderDto> orderDtos){
        Map<Exchange, List<OrderDto>> result = new HashMap<>();
        for(Exchange e : Exchange.getExchanges()){
            List<OrderDto> dtos = orderDtos.stream().filter((o) ->
                    o.getExchangeName().equals(e.getName())).collect(Collectors.toList());

            if(!dtos.isEmpty()) result.put(e, dtos);
        }

        return result;
    }

    private Map<Language, List<OrderDto>> sortByLanguage(List<OrderDto> orderDtos){
        Map<Language, List<OrderDto>> result = new HashMap<>();
        for(Language l : Language.getLanguages()){
            List<OrderDto> dtos = orderDtos.stream().filter((o) ->
                    o.getSubscription().equals(l.getName())).collect(Collectors.toList());

            if(!dtos.isEmpty()) result.put(l, dtos);
        }
        return result;
    }


    private String getOrderInfo(OrderDto dto){
        return "<a href=" + "'"+dto.getExchangeLink() + dto.getLink() + "'"+">" + dto.getTitle() + "</a>";


    }

    public static String delim() {
        String delim = "\n.";
        for(int i = 0; i < 70; i ++){
            delim += ".";
        }
    return delim + "\n";
    }

}
