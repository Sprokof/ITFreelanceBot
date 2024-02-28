package telegramBot.command;


import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;

public class InfoCommand implements Command {
    private static final String INFO_COMMAND = "Собираю заказы со следующих бирж: freelance.habr.com, fl.ru, kwork.ru"+
            "\nпо следующим языкам: JavaScript, Python, Java, Ruby, PHP, C (включает C++, C#)" +
            "\n" + MessageService.delim() +
            "\nПоддерживаю следующие комманды" +
            "\nКоманда /add - добавление языка для подписки." +
            "\nКоманда /remove - удаления языка подписки." +
            "\nКоманда /latest - последние заказы." +
            "\nКоманда /subs - текущие подписки." +
            "\nКоманда /stop - остановка уведомлений о новых заказах." +
            "\nКоманда /restart - возообновление уведомлений о новых заказах.";

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
