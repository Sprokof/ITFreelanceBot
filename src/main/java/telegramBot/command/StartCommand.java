package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import telegramBot.service.MessageService;

@Component
public class StartCommand implements Command {
    private static final String START_COMMAND = "Помогаю с мониторингом новых заказов на фриланс биржах по интересующему языку программирования.\n " +
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
