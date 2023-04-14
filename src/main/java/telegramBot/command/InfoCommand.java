package telegramBot.command;


import telegramBot.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoCommand implements Command {
    private static final String INFO_COMMAND = "Команда /add - добавление языка для подписки." +
            "\nКоманда /remove - удаления языка подписки." +
            "\nКоманда /stop - остановка уведомлений об новых заказах." +
            "\nКоманда /restart - возообновление уведомлений об новых заказах.";

    private final MessageService messageService;


    public InfoCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, INFO_COMMAND);

    }
}
