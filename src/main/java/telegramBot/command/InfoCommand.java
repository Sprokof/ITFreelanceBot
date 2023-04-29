package telegramBot.command;


import telegramBot.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.services.MessageServiceImpl;

public class InfoCommand implements Command {
    private static final String INFO_COMMAND = "Собираю заказы со следующих бирж: freelance.habr.com, fl.ru"+
            "\nследующим языкам: Java, Python, JavaScript, PHP "+ MessageServiceImpl.delim() +
            "Поддерживаю следующие комманды" +
            "\nКоманда /add - добавление языка для подписки." +
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
