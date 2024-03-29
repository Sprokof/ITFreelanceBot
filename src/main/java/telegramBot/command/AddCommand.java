package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;

public class AddCommand implements Command {
    private static final String ADD_COMMAND = "Введите название языка программирования, " +
            "заказы на который хотите получать";
    private final MessageService messageService;

    public AddCommand(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, ADD_COMMAND);
    }
}
