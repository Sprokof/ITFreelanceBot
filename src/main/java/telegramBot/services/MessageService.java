package telegramBot.services;

import telegramBot.dto.OrderDto;

import java.util.List;

public interface MessageService {
    void sendResponse(String userChatId, String message);
    void sendNotice(String userChatId, List<OrderDto> dto);
}
