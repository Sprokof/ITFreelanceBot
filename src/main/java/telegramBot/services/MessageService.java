package telegramBot.services;

import telegramBot.dto.OrderDto;

import java.util.Set;

public interface MessageService {
    void sendResponse(String userChatId, String message);
    void sendNotice(String userChatId, Set<OrderDto> dto);
}
