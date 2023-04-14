package telegramBot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.services.MessageService;
import telegramBot.services.UserService;
import telegramBot.services.UserServiceImpl;

public class StopCommand implements Command {
    private static final String STOP_COMMAND = "Вы остановили уведомления. /restart - возобновление";

    private final MessageService messageService;

    private final UserService userService = new UserServiceImpl();

    public StopCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        userService.setActive(chatId, false);
        this.messageService.sendResponse(chatId, STOP_COMMAND);

    }
}
