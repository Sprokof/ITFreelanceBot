package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;
import telegramBot.util.BotUtil;

public class UnknownCommand implements Command {

    private static final String UNKNOWN_COMMAND = BotUtil.UNKNOWN_COMMAND;

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
