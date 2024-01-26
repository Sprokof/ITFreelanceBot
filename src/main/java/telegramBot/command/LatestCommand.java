package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;

public class LatestCommand implements Command {
    private static final String LATEST_COMMAND = "Введите название языка и число " +
            "последних заказов в формате (language, count)";

    private final MessageService messageService;

    public LatestCommand(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, LATEST_COMMAND);
    }

}
