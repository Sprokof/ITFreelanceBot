package telegramBot.command;


import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.enums.Role;
import telegramBot.service.MessageService;

public class InfoCommand implements Command {
    private static final StringBuilder INFO_COMMAND = new StringBuilder();

    static {
        INFO_COMMAND.append("Собираю заказы со следующих бирж: freelance.habr.com, fl.ru, kwork.ru")
                .append("\nпо следующим языкам: JavaScript, Python, Java, Ruby, PHP, C (включает C++, C#)\n")
                .append(MessageService.delim())
                .append("\nПоддерживаю следующие комманды")
                .append("\nКоманда /add - добавление языка для подписки.")
                .append("\nКоманда /remove - удаления языка подписки.")
                .append("\nКоманда /latest - последние заказы.")
                .append("\nКоманда /subs - текущие подписки.")
                .append("\nКоманда /stop - остановка уведомлений о новых заказах.")
                .append("\nКоманда /restart - возообновление уведомлений о новых заказах.");
    }
    private static final String ADMIN_LINE = "\nКоманда /admin - статистика по системе";

    private final MessageService messageService;


    public InfoCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, (Role.isAdmin(chatId) ? (INFO_COMMAND.append(ADMIN_LINE).toString()) : INFO_COMMAND.toString()));
    }
}
