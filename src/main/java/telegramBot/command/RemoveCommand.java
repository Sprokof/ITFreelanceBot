package telegramBot.command;


import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;

public class RemoveCommand implements Command {
    private static final String REMOVE_COMMAND = "Введите название языка, подписку на который хотите убрать";

    private final MessageService messageService;

    public RemoveCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, REMOVE_COMMAND);
    }
}
