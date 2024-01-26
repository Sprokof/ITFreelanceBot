package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;

public class UnknownCommand implements Command {

    private static final String UNKNOWN_COMMAND = "Unknown command. /info - получение сводки";

    private final MessageService messageService;

    public UnknownCommand (MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, UNKNOWN_COMMAND);
    }
}
