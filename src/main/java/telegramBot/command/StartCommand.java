package telegramBot.command;

import telegramBot.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import telegramBot.services.UserService;
import telegramBot.services.UserServiceImpl;

@Component
public class StartCommand implements Command {
    private static final String START_COMMAND = "Собираю заказы по заданному языку програмиирования на " +
            "следующих биржах -> freelance.habr.com и fl.ru, " +
            "поддерживаю следующие языки -> Java, Python, JavaScript." +
            "\nКоманда /info для получения сводки.";


    private final MessageService messageService;

    public StartCommand(MessageService messageService){
        this.messageService = messageService;
    }
    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.messageService.sendResponse(chatId, START_COMMAND);

    }
}
